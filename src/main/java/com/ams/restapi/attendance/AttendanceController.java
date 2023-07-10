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

/**
 * Attendance Record Management Endpoints
 * @author Ryan Woo (rtwoo)
 */
@RestController
class AttendanceController {
    
    private final AttendanceRepository repository;

    AttendanceController(AttendanceRepository repository) {
        this.repository = repository;
    }

    // Multi-item

    @GetMapping("/attendance")
    List<AttendanceLog> search(
        @RequestParam("room") Optional<String> room,
        @RequestParam("date") Optional<LocalDate> date,
        @RequestParam("startTime") Optional<LocalTime> startTime,
        @RequestParam("endTime") Optional<LocalTime> endTime,
        @RequestParam("sid") Optional<String> sid,
        @RequestParam("type") Optional<String> type,
        @RequestParam("page") int page, 
        @RequestParam("size") int size) {
            Pageable pageable = PageRequest.of(page, size);

            Page<AttendanceLog> result = repository.search(
                room.orElse(null),
                date.orElse(null),
                startTime.orElse(null),
                endTime.orElse(null),
                sid.orElse(null),
                type.orElse(null),
                pageable
            );
            // if (date.isPresent() && sid.isPresent() && start.isPresent() && end.isPresent())
            //     result = repository.findByDateAndSidAndTimeBetween(
            //         date.get(), sid.get(), start.get(), end.get(), pageable);
            // else if (date.isPresent() && start.isPresent() && end.isPresent())
            //     result = repository.findByDateAndTimeBetween(date.get(), start.get(), end.get(), pageable);
            // else if (sid.isPresent())
            //     result = repository.findBySid(sid.get(), pageable);
            // else result = repository.findAll(pageable);

            if (page > result.getTotalPages()) {
                throw new AttendanceLogPageOutofBoundsException(page, size);
            }
            return result.getContent();
    }

    @PostMapping("/attendance")
    AttendanceLog createSingle(@RequestBody AttendanceLog newLog) {
        return repository.save(newLog);
    }

    // Single item

    @GetMapping("/attendance/{id}")
    AttendanceLog getSingle(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new AttendanceLogNotFoundException(id));
    }

    @PutMapping("/attendance/{id}")
    AttendanceLog updateSingle(@PathVariable Long id, @RequestBody AttendanceLog newLog) {
        newLog.setId(id);
        return repository.save(newLog);
    }

    @DeleteMapping("/attendance/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
