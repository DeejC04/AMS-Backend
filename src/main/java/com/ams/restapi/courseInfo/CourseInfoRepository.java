package com.ams.restapi.courseInfo;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ams.restapi.timeConfig.TimeConfig;

public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {
    @Query("SELECT c.defaultTimeConfig FROM CourseInfo c join c.daysOfWeek d "
    + "inner join c.defaultTimeConfig conf WHERE "
    + "(:room = c.room) and "
    + "(:dayOfWeek = d) and "
    + "(:time between conf.beginIn and conf.endOut)")
    Optional<TimeConfig> resolve(
        @Param("room") String room,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("time") LocalTime time);
}