package com.ams.restapi.courseInfo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing course metadata
 * 
 * @author Harwinder Singh
 */
@RestController
public class CourseInfoController {
    private final CourseInfoRespository repository;

    public CourseInfoController(CourseInfoRespository repository) {
        this.repository = repository;
    }

    @GetMapping("/courseInfo/{courseID}")
    CourseInfo search(@PathVariable Long courseID) {
        return repository.findById(courseID)
                .orElseThrow(() -> new CourseInfoNotFoundException(courseID));
    }

    @PutMapping("/courseInfo/{courseID}")
    CourseInfo update(@PathVariable Long courseID,
            @RequestBody CourseInfo newInfo) {
        newInfo.setCourseId(courseID);
        return repository.save(newInfo);
    }

    @DeleteMapping("/courseInfo/{courseID}")
    void delete(@PathVariable Long courseID) {
        repository.deleteById(courseID);
    }
}
