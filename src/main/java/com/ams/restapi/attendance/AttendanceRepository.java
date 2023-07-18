package com.ams.restapi.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    @Query("SELECT a FROM AttendanceLog a WHERE "
    + "(:room is null or a.room = :room) and "
    + "(:date is null or a.date = :date) and "
    + "( ( (:startTime is not null and :endTime is not null) "
    + "       and (a.time between :startTime and :endTime) ) or "
    + "  (:startTime is not null and :endTime is null and a.time >= :startTime) or "
    + "  (:endTime is not null and :startTime is null and a.time <= :endTime) or "
    + "  (:startTime is null and :endTime is null) ) and "
    + "(:sid is null or a.sid = :sid) and "
    + "(:type is null or a.type = :type)")
    Page<AttendanceRecord> search(
        @Param("room") String room,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime,
        @Param("sid") String sid,
        @Param("type") String type,
        Pageable pageable);

    List<AttendanceRecord> findByRoomAndDateAndTimeBetween(
        String room, LocalDate date,
        LocalTime startTime, LocalTime endTime);

    List<AttendanceRecord> findByRoomAndDateAndTimeBetweenAndSid(
        String room, LocalDate date,
        LocalTime startTime, LocalTime endTime, String sid);
}
