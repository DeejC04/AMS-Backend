package com.ams.restapi.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.restapi.courseInfo.CourseInfoRespository;

import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Attendance Record Management endpoints
 * @author Ryan Woo (rtwoo)
 */
@RestController
class AttendanceController {
    
    private final AttendanceRepository repository;
    private final CourseInfoRespository courseInfo;

    AttendanceController(AttendanceRepository repository, CourseInfoRespository courseInfo) {
        this.repository = repository;
        this.courseInfo = courseInfo;
    }

    // Multi-item

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/attendance")
    List<AttendanceRecord> search(
        @RequestParam("room") Optional<String> room,
        @RequestParam("date") Optional<LocalDate> date,
        @RequestParam("startTime") Optional<LocalTime> startTime,
        @RequestParam("endTime") Optional<LocalTime> endTime,
        @RequestParam("sid") Optional<String> sid,
        @RequestParam("type") Optional<String> type,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") Optional<String> sortBy,
        @RequestParam("sortType") Optional<String> sortType) {
            Pageable pageable;
            if (sortBy.isPresent())
                pageable = PageRequest.of(page, size,
                    Sort.by(sortType.orElse("asc").equals("desc") ? Direction.DESC : Direction.ASC,
                    sortBy.get()));
            else
                pageable = PageRequest.of(page, size);

            Page<AttendanceRecord> result = repository.search(
                room.orElse(null),
                date.orElse(null),
                startTime.orElse(null),
                endTime.orElse(null),
                sid.orElse(null),
                type.orElse(null),
                pageable
            );

            if (page > result.getTotalPages()) {
                throw new AttendanceLogPageOutofBoundsException(page, size);
            }
            return result.getContent();
    }

    // Single item

    @PostMapping("/attendance")
    AttendanceRecordDTO createSingle(@RequestBody AttendanceRecordDTO newLog) {

        // courseInfo.findAll(null, null);

        return new AttendanceRecordDTO(repository.save(newLog.toEntity()));
    }

    @GetMapping("/attendance/{id}")
    AttendanceRecord getSingle(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new AttendanceLogNotFoundException(id));
    }

    @PutMapping("/attendance/{id}")
    AttendanceRecord updateSingle(@PathVariable Long id, @RequestBody AttendanceRecord newLog) {
        newLog.setId(id);
        return repository.save(newLog);
    }

    @DeleteMapping("/attendance/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
