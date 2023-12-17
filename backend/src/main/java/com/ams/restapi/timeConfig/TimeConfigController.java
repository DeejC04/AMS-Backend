package com.ams.restapi.timeConfig;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.restapi.courseInfo.CourseInfo;
import com.ams.restapi.courseInfo.CourseInfoRepository;

/**
 * Course specific time configuration endpoints
 * @author Gabriel Esparza Uriarte
 * @author Ryan Woo (rtwoo)
 */
@RestController
class TimeConfigController {

    private final CourseInfoRepository courseInfoRepo;
    private final DateSpecificTimeRepository dateSpecificTimeRepo;

    TimeConfigController(CourseInfoRepository courseInfoRepo,
            DateSpecificTimeRepository dateSpecificTimeRepo) {
        this.courseInfoRepo = courseInfoRepo;
        this.dateSpecificTimeRepo = dateSpecificTimeRepo;
    }

    @GetMapping("/timeConfig/{courseID}")
    TimeConfigDTO search(@PathVariable Long courseID) {
        CourseInfo info = courseInfoRepo.findById(courseID)
            .orElseThrow(() ->
            new TimeConfigNotFoundException(courseID));
        return new TimeConfigDTO(info.getDefaultTimeConfig());
    }

    @PutMapping("/timeConfig/{courseID}")
    TimeConfigDTO update(@PathVariable Long courseID,
            @Valid @RequestBody TimeConfigDTO timeConfig) {
        CourseInfo info = courseInfoRepo.findById(courseID).orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        TimeConfig config = timeConfig.toEntity(info);
        config.setId(info.getDefaultTimeConfig().getId());
        info.setDefaultTimeConfig(config);
        return new TimeConfigDTO(courseInfoRepo.save(info).getDefaultTimeConfig());
    }

    @DeleteMapping("/timeConfig/{courseID}")
    TimeConfigDTO delete(@PathVariable Long courseID) {
        CourseInfo info = courseInfoRepo.findById(courseID).orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        info.setDefaultTimeConfig(CourseInfo.getDefaultTimeConfig(info, info.getStartTime(), info.getEndTime()));
        return new TimeConfigDTO(courseInfoRepo.save(info).getDefaultTimeConfig());
    }

    @GetMapping("/timeConfig/{courseID}/{date}")
    TimeConfigDTO getTimeConfig(@PathVariable Long courseID, @PathVariable LocalDate date) {
        CourseInfo course = courseInfoRepo.findById(courseID)
            .orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        return new TimeConfigDTO(dateSpecificTimeRepo.findByCourseAndDate(course, date)
            .orElseThrow(() -> new DateSpecificTimeConfigNotFoundException(courseID, date)).getConfig());
    }

    @PutMapping("/timeConfig/{courseID}/{date}")
    TimeConfigDTO newTimeConfigAndDate(@PathVariable Long courseID, @PathVariable LocalDate date,
            @RequestBody TimeConfigDTO timeConfig) {
        CourseInfo course = courseInfoRepo.findById(courseID)
            .orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        
        Optional<DateSpecificTimeConfig> config = dateSpecificTimeRepo.findByCourseAndDate(course, date);

        if (config.isPresent()) {
            config.get().setConfig(timeConfig.toEntity(course));
            return new TimeConfigDTO(dateSpecificTimeRepo.save(config.get()).getConfig());
        }

        return new TimeConfigDTO(dateSpecificTimeRepo.save(
            new DateSpecificTimeConfig(course, date, timeConfig.toEntity(course))).getConfig());

    }


    @DeleteMapping("/timeConfig/{courseID}/{date}")
    void deleteTimeConfig(@PathVariable Long courseID, @PathVariable LocalDate date) {
        CourseInfo course = courseInfoRepo.findById(courseID)
            .orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        DateSpecificTimeConfig config = dateSpecificTimeRepo.findByCourseAndDate(course, date)
            .orElseThrow(() -> new DateSpecificTimeConfigNotFoundException(courseID, date));
        dateSpecificTimeRepo.delete(config);
    }

    @GetMapping("/timeConfig/test")
    TimeConfig resolveTest(@RequestParam("room") String room,
            @RequestParam("date") LocalDate date,
            @RequestParam("time") LocalTime time) {
        return dateSpecificTimeRepo.resolve(room, date, time)
            .orElseThrow(() -> new RuntimeException("oogao"));
    }

}
