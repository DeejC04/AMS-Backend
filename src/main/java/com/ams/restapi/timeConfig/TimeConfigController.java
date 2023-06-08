package com.ams.restapi.timeConfig;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TimeConfigController {

    private final TimeRepository timerepo;

    TimeConfigController(TimeRepository repository) {
        timerepo = repository;
    }

    @PutMapping("/timeConfig/{courseID}")
    TimeConfig newTimeConfig(@PathVariable Long courseID, @RequestBody TimeConfig newTC) {
        newTC.setCourseID(courseID);
        return timerepo.save(newTC);
    }

    @GetMapping("/timeConfig/{courseID}")
    TimeConfig search(@PathVariable Long courseID) {
        return timerepo.findById(courseID).orElse(null);
    }

    @DeleteMapping("/timeConfig/{courseID}")
    void deleteTimeConfig(@PathVariable Long courseID) {
        timerepo.deleteById(courseID);
    }

    // // @PostMapping("/timeConfig/{courseID}/{date}")
    // @PutMapping("/timeConfig/{courseID}/{date}")
    // TimeConfig newTimeConfigAndDate(@PathVariable Long courseID, @PathVariable int date,
    //         @RequestBody TimeConfig newTC) {
    //     TimeConfig tc = timerepo.findByCourseIDAndDate(courseID, date);
    //     if (tc == null) {
    //         return timerepo.save(newTC);
    //     }
    //     deleteTimeConfig(courseID, date);
    //     return timerepo.save(newTC);
    // }

    // @GetMapping("/timeConfig/{courseID}/{date}")
    // TimeConfig getTimeConfig(@PathVariable Long courseID, @PathVariable int date) {
    //     TimeConfig tc = timerepo.findByCourseIDAndDate(courseID, date);
    //     return tc;
    // }

    // @DeleteMapping("/timeConfig/{courseID}/{date}")
    // void deleteTimeConfig(@PathVariable Long courseID, @PathVariable int date) {
    //     TimeConfig tc = timerepo.findByCourseIDAndDate(courseID, date);
    //     timerepo.delete(tc);
    // }

}
