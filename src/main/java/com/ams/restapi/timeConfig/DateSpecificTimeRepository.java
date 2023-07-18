package com.ams.restapi.timeConfig;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.restapi.courseInfo.CourseInfo;

public interface DateSpecificTimeRepository
        extends JpaRepository<DateSpecificTimeConfig, Long> {
    Optional<DateSpecificTimeConfig> findByCourseAndDate(CourseInfo course, LocalDate date);
}