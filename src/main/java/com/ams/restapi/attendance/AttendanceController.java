package com.ams.restapi.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AttendanceController {
    
    private final AttendanceRepository repository;

    AttendanceController(AttendanceRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/attendance")
    List<AttendanceLog> search(
        @RequestParam("sid") Optional<String> sid,
        @RequestParam("date") Optional<LocalDate> date,
        @RequestParam("startTime") Optional<LocalTime> start,
        @RequestParam("endTime") Optional<LocalTime> end,
        @RequestParam("page") int page, 
        @RequestParam("size") int size) {
            Pageable pageable = PageRequest.of(page, size);

            Page<AttendanceLog> result;
            if (date.isPresent() && sid.isPresent() && start.isPresent() && end.isPresent())
                result = repository.findByDateAndSidAndTimeBetween(
                    date.get(), sid.get(), start.get(), end.get(), pageable);
            else if (date.isPresent() && start.isPresent() && end.isPresent())
                result = repository.findByDateAndTimeBetween(date.get(), start.get(), end.get(), pageable);
            else if (sid.isPresent())
                result = repository.findBySid(sid.get(), pageable);
            else result = repository.findAll(pageable);

            if (page > result.getTotalPages()) {
                // TODO: Throw exception
            }
            return result.getContent();
    }
    // end::get-aggregate-root[]

    @PostMapping("/attendance")
    AttendanceLog newAttendanceLog(@RequestBody AttendanceLog newLog) {
        return repository.save(newLog);
    }

    // Single item

    @GetMapping("/attendance/{id}")
    AttendanceLog one(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new AttendanceLogNotFoundException(id));
    }

    @PutMapping("/attendance/{id}")
    AttendanceLog replaceEmployee(@RequestBody AttendanceLog newLog, @PathVariable Long id) {
        AttendanceLog log = repository.findById(id).orElseGet(() -> {
            newLog.setId(id);
            return repository.save(newLog);
        });
        log.setRoom(newLog.getRoom());
        log.setTime(newLog.getTime());
        log.setSid(newLog.getSid());
        log.setType(newLog.getType());
        return repository.save(log);
    }

    @DeleteMapping("/attendance/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
