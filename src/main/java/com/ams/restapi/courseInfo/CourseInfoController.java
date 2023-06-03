package com.ams.restapi.courseInfo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseInfoController {
    private final CourseInfoRespository repository;

    public CourseInfoController(CourseInfoRespository repository) {
        this.repository = repository;
    }

    @GetMapping("/courseInfo/{courseID}")
    CourseInfoLog search(@PathVariable Long courseID) {
        List<CourseInfoLog> log = repository.findByCourseId(courseID);
        if (log.isEmpty())
            throw new CourseInfoLogNotFoundException(courseID);
        else
            return log.get(0);
    }

    @PutMapping("/courseInfo/{courseID}")
    CourseInfoLog newCourseInfoLog(@RequestBody CourseInfoLog newLog, @PathVariable Long courseID) {
        List<CourseInfoLog> log = repository.findByCourseId(courseID);
        if (!log.isEmpty()) {
            CourseInfoLog updateLog = log.get(0);
            updateLog.setCourseName(newLog.getCourseName());
            updateLog.setDaysOfWeek(newLog.getDaysOfWeek());
            updateLog.setEndTime(newLog.getEndTime());
            updateLog.setStartTime(newLog.getStartTime());
            updateLog.setRoom(newLog.getRoom());
            return repository.save(updateLog);
        } else {
            newLog.setCourseId(courseID);
            return repository.save(newLog);
        }
    }

    @DeleteMapping("/courseInfo/{courseID}")
    void deleteEmployee(@PathVariable Long courseID) {
        repository.deleteById(courseID);
    }
}
