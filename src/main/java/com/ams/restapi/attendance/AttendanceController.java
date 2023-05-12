package com.ams.restapi.attendance;

import java.util.List;
import java.util.Optional;

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
        @RequestParam("startTime") Optional<Long> start,
        @RequestParam("endTime") Optional<Long> end) {
            System.out.println("AAA");
            if (sid.isPresent() && start.isPresent() && end.isPresent())
                return repository.findBySidAndTimeBetween(sid.get(), start.get(), end.get());
            if (start.isPresent() && end.isPresent())
                return repository.findByTimeBetween(start.get(), end.get());
            if (sid.isPresent()) return repository.findBySid(sid.get());
            return repository.findAll();
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
