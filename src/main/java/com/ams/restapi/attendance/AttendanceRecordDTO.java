package com.ams.restapi.attendance;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AttendanceRecordDTO {
    private Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String room;
    private String date;
    private String time;
    @JsonInclude(Include.NON_NULL)
    private Long timestamp;
    private String sid;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttendanceRecordDTO() {}
    
    public AttendanceRecordDTO(AttendanceRecord record) {
        id = record.getId();
        room = record.getRoom();
        date = record.getDate().toString();
        time = record.getTime().toString();
        sid = record.getSid();
        type = record.getType();
    }

    public AttendanceRecord toEntity(LocalDate date, LocalTime time,
        AttendanceRecord.AttendanceType type) {
        
        return new AttendanceRecord(room, date, time, sid, type.toString());
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
