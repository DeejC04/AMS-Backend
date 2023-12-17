package com.ams.restapi.courseInfo;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.restapi.timeConfig.TimeConfig;

/**
 * Controller for managing course metadata
 * @author Harwinder Singh
 * @author Ryan Woo (rtwoo)
 */
@RestController
public class CourseInfoController {
    private final CourseInfoRepository repository;

    public CourseInfoController(CourseInfoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/courseInfo/{courseID}")
    CourseInfoDTO search(@PathVariable Long courseID) {
        CourseInfo course = repository.findById(courseID)
                .orElseThrow(() -> new CourseInfoNotFoundException(courseID));
        return new CourseInfoDTO(course);
    }

    @PutMapping("/courseInfo/{courseID}")
    CourseInfoDTO update(@PathVariable Long courseID,
            @RequestBody CourseInfoDTO newInfo) {
        return new CourseInfoDTO(repository.save(newInfo.toEntity(courseID)));
    }

    @DeleteMapping("/courseInfo/{courseID}")
    ResponseEntity<String> delete(@PathVariable Long courseID) {
        if (!repository.existsById(courseID)) throw new CourseInfoNotFoundException(courseID);

        repository.deleteById(courseID);
        return ResponseEntity.ok("Deleted course info log " + courseID);
    }

    @GetMapping("/courseInfo/test")
    TimeConfig resolveTest(@RequestParam("room") String room,
            @RequestParam("date") LocalDate date,
            @RequestParam("time") LocalTime time) {
        return repository.resolve(room, date.getDayOfWeek(), time)
            .orElseThrow(() -> new RuntimeException("oogao"));
    }
}
