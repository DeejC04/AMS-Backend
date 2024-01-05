package com.ams.restapi.attendance;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.restapi.attendance.AttendanceRecord.AttendanceType;
import com.ams.restapi.courseInfo.CourseInfoRepository;
import com.ams.restapi.timeConfig.DateSpecificTimeConfig;
import com.ams.restapi.timeConfig.DateSpecificTimeRepository;
import com.ams.restapi.timeConfig.TimeConfig;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


/**
 * Attendance Record Management endpoints
 * @author Ryan Woo (rtwoo)
 */
@RestController
class AttendanceController {
    
    private final AttendanceRepository repository;
    private final CourseInfoRepository courseInfo;
    private final DateSpecificTimeRepository dateConfigs;

    @PersistenceContext
    private EntityManager eManager;
    
    AttendanceController(AttendanceRepository repository,
        CourseInfoRepository courseInfo, DateSpecificTimeRepository dateConfigs) {
        this.repository = repository;
        this.courseInfo = courseInfo;
        this.dateConfigs = dateConfigs;
    }

    // Multi-item

    @GetMapping("/attendance")
    ResponseEntity<List<AttendanceRecordDTO>> search(
        @RequestParam("room") Optional<String> room,
        @RequestParam("date") Optional<LocalDate> date,
        @RequestParam("startTime") Optional<LocalTime> startTime,
        @RequestParam("endTime") Optional<LocalTime> endTime,
        @RequestParam("sid") Optional<String> sid,
        @RequestParam("types") Optional<List<AttendanceType>> types,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") Optional<String> sortBy,
        @RequestParam("sortType") Optional<String> sortType) {
            
            if (room == null || room.isEmpty() || date == null || date.isEmpty())
                throw new AttendanceRecordPostInvalidException("Missing some/all required fields");
            Pageable pageable = PageRequest.of(page, size);

            CriteriaBuilder criteriaBuilder = eManager.getCriteriaBuilder();
            CriteriaQuery<AttendanceRecord> criteriaQuery = criteriaBuilder.createQuery(AttendanceRecord.class);
            Root<AttendanceRecord> from = criteriaQuery.from(AttendanceRecord.class);

            CriteriaQuery<AttendanceRecord> select = criteriaQuery.select(from);
            List<Predicate> predicates = genPredicates(room, date, startTime, endTime, sid, types,
                criteriaBuilder, from);
            
            if (sortType.isPresent() && sortBy.isPresent()) {
                if (sortType.get().equals("desc")) {
                    select.orderBy(criteriaBuilder.desc(from.get(sortBy.get())));
                } else {
                    select.orderBy(criteriaBuilder.asc((from.get(sortBy.get()))));      
                }
            }
            select.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new)));
            

            // TypedQuery<AttendanceRecord> typedQuery = eManager.createQuery(select);
            // List<AttendanceRecord> result = typedQuery.getResultList();
            // Long count = (long)eManager.createQuery(select).getResultList().size();
            List<AttendanceRecord> result = eManager.createQuery(select)
                .setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
                .getResultList();

            // Fetches the count of all AttendanceRecords as per given criteria
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<AttendanceRecord> countFrom = countQuery.from(AttendanceRecord.class);
            countQuery.select(criteriaBuilder.count(countFrom));
            List<Predicate> countPredicates = genPredicates(room, date, startTime, endTime, sid, types,
                criteriaBuilder, countFrom);
            // * not necessary to sort when just counting
            countQuery.where(criteriaBuilder.and(countPredicates.toArray(Predicate[]::new)));
            Long count = eManager.createQuery(countQuery).getSingleResult();

            Page<AttendanceRecord> pResult = new PageImpl<>(result, pageable, count);

            if (page >= pResult.getTotalPages()) {
                throw new AttendanceLogPageOutofBoundsException(page, size);
            }

            return ResponseEntity.ok()
                .header("Total-Pages", Integer.toString(pResult.getTotalPages()))
                .body(pResult.getContent().stream().map(AttendanceRecordDTO::new)
                    .collect(Collectors.toList()));
    }

    private List<Predicate> genPredicates(Optional<String> room, Optional<LocalDate> date,
            Optional<LocalTime> startTime, Optional<LocalTime> endTime, Optional<String> sid,
            Optional<List<AttendanceType>> types, CriteriaBuilder criteriaBuilder, Root<AttendanceRecord> from) {
        List<Predicate> predicates = new ArrayList<>();
        if (room.isPresent())
            predicates.add(criteriaBuilder.equal(from.get("room"), room.get()));
        if (date.isPresent())
            predicates.add(criteriaBuilder.equal(from.get("date"), date.get()));
        if (startTime.isPresent() && endTime.isPresent())
            predicates.add(criteriaBuilder.between(from.get("time"), startTime.get(), endTime.get()));
        if (startTime.isPresent())
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.get("time"), startTime.get()));
        if (endTime.isPresent())
            predicates.add(criteriaBuilder.lessThanOrEqualTo(from.get("time"), endTime.get()));
        if (sid.isPresent()) predicates.add(criteriaBuilder.equal(from.get("sid"), sid.get()));
        if (types.isPresent() && types.get().size() > 0) predicates.add(from.get("type").in(types.get()));
        return predicates;
    }

    // Single item

    @PostMapping("/attendance")
    AttendanceRecordDTO createSingle(@Valid @RequestBody AttendanceRecordDTO newLog) {
        
        LocalDate rDate;
        LocalTime rTime;

        try {
            if (newLog.getTimestamp() != null) {    
                LocalDateTime triggerTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(newLog.getTimestamp()),
                    ZoneId.of("MST", ZoneId.SHORT_IDS)); 
                rDate = triggerTime.toLocalDate();
                rTime = triggerTime.toLocalTime();
            } else {
                rDate = LocalDate.parse(newLog.getDate());
                rTime = LocalTime.parse(newLog.getTime());
            }
        } catch(DateTimeException e) {
            e.printStackTrace();
            return null;
        }

        TimeConfig config;
        try {
            System.out.println(newLog.getRoom()
                + " " + rDate.getDayOfWeek()
                + " " + rTime);
            config =
                dateConfigs.resolve(newLog.getRoom(), rDate, rTime)
                    .orElseGet(() ->
                    courseInfo.resolve(newLog.getRoom(), rDate.getDayOfWeek(), rTime).get()
                );
            
            // * just in case we missed the date specific time config
            // * resolve it from the course relation accessible from the resolved default
            Optional<DateSpecificTimeConfig> check = dateConfigs.findByCourseAndDate(config.getCourse(), rDate);
            if (check.isPresent())
                config = check.get().getConfig();

        } catch (NoSuchElementException e) {
            System.out.printf("%s, %s, %s",
                newLog.getRoom(), rDate.toString(), rDate.getDayOfWeek(), rTime.toString());
            e.printStackTrace(System.out);
            throw new AttendanceRecordPostInvalidException("Failed to resolve time config for the given datetime");
        }

        AttendanceRecord.AttendanceType rType;
        if (rTime.isBefore(config.getBeginIn()) || rTime.isAfter(config.getEndOut())) {
            rType = AttendanceType.INVALID;
        } else {
            List<AttendanceRecord> previousScans = repository.findByRoomAndDateAndTimeBetweenAndSid(
                newLog.getRoom(), rDate, config.getBeginIn(), config.getEndOut(), newLog.getSid());

            if (previousScans.size() == 0) { // ARRIVE
                if (rTime.isAfter(config.getBeginIn().minusMinutes(1L)) &&
                        rTime.isBefore(config.getEndIn().plusMinutes(1L))) {
                    rType = AttendanceRecord.AttendanceType.ARRIVED;
                } else if (rTime.isAfter(config.getEndIn()) &&
                        rTime.isBefore(config.getEndLate().plusMinutes(1L))) {
                    rType = AttendanceRecord.AttendanceType.ARRIVED_LATE;
                } else {
                    rType = AttendanceRecord.AttendanceType.ARRIVED_INVALID;
                }
            } else if (previousScans.size() == 1) { // LEAVE
                if (rTime.isAfter(config.getBeginOut().minusMinutes(1L)) &&
                        rTime.isBefore(config.getEndOut().plusMinutes(1L))) {
                    rType = AttendanceRecord.AttendanceType.LEFT;
                } else {
                    rType = AttendanceRecord.AttendanceType.LEFT_INVALID;
                }
            } else { // INVALID
                rType = AttendanceRecord.AttendanceType.INVALID;
            }
        }

        AttendanceRecord record = newLog.toEntity(rDate, rTime, rType);

        System.out.println("Successfully received POST, sending response...");
        
        return new AttendanceRecordDTO(repository.save(record));
    }

    @GetMapping("/attendance/{id}")
    AttendanceRecordDTO getSingle(@PathVariable Long id) {
        return new AttendanceRecordDTO(repository.findById(id)
            .orElseThrow(() -> new AttendanceLogNotFoundException(id)));
    }

    @PutMapping("/attendance/{id}")
    AttendanceRecordDTO updateSingle(@PathVariable Long id, @Valid @RequestBody AttendanceRecord newLog) {
        if (!repository.existsById(id)) throw new AttendanceLogNotFoundException(id);
        
        newLog.setId(id);
        return new AttendanceRecordDTO(repository.save(newLog));
    }

    @DeleteMapping("/attendance/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new AttendanceLogNotFoundException(id);

        repository.deleteById(id);
        return ResponseEntity.ok("Deleted attendance log " + id);
    }

}
