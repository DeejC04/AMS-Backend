package com.ams.restapi.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByRoomAndDateAndTimeBetween(
        String room, LocalDate date,
        LocalTime startTime, LocalTime endTime);

    List<AttendanceRecord> findByRoomAndDateAndTimeBetweenAndSid(
        String room, LocalDate date,
        LocalTime startTime, LocalTime endTime, String sid);
}
