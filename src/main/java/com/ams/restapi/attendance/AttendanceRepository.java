package com.ams.restapi.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface AttendanceRepository extends JpaRepository<AttendanceLog, Long> {
    Page<AttendanceLog> findBySid(String sid, Pageable pageable);
    Page<AttendanceLog> findByDateAndSidAndTimeBetween(
        LocalDate date, String sid,
        LocalTime startTime, LocalTime endTime, Pageable pageable);
    Page<AttendanceLog> findByDateAndTimeBetween(
        LocalDate date, LocalTime startTime, LocalTime endTime, Pageable pageable);
}
