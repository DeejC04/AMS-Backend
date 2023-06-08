package com.ams.restapi.timeConfig;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface TimeRepository extends JpaRepository<TimeConfig, Long> {
    // List<TimeConfig> findByCourseID(Long courseID);

    // TimeConfig findByCourseIDAndDate(Long courseID, Integer date);

}