package com.ams.restapi.attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface AttendanceRepository extends JpaRepository<AttendanceLog, Long> {
    List<AttendanceLog> findBySid(String sid);
    List<AttendanceLog> findBySidAndTimeBetween(String sid, Long startTime, Long endTime);
    List<AttendanceLog> findByTimeBetween(Long startTime, Long endTime);
}
