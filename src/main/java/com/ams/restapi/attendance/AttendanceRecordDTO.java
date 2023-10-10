package com.ams.restapi.attendance;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ams.restapi.attendance.AttendanceRecord.AttendanceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotNull;


public class AttendanceRecordDTO {
    private Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Reference: https://www.appsdeveloperblog.com/validate-request-body-in-restful-web-service/

    @NotNull(message = "Room cannot be missing or empty")
    private String room;

    @NotNull(message = "Date cannot be missing or empty")
    private String date;

    @NotNull(message = "Time cannot be missing or empty")
    private String time;

    @NotNull(message = "TimeStamp cannot be missing or empty")
    @JsonInclude(Include.NON_NULL)
    private Long timestamp;

    @NotNull(message = "Student ID cannot be missing or empty")
    private String sid;

    @NotNull(message = "Type cannot be missing or empty")
    private AttendanceType type;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttendanceType getType() {
        return type;
    }

    public void setType(AttendanceType type) {
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
        AttendanceType type) {
        
        return new AttendanceRecord(room, date, time, sid, type);
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
