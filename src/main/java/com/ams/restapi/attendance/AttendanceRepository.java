package com.ams.restapi.attendance;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface AttendanceRepository extends JpaRepository<AttendanceLog, Long> {
    Page<AttendanceLog> findBySid(String sid, Pageable pageable);
    Page<AttendanceLog> findBySidAndTimeBetween(String sid, Long startTime, Long endTime, Pageable pageable);
    Page<AttendanceLog> findByTimeBetween(Long startTime, Long endTime, Pageable pageable);
}
