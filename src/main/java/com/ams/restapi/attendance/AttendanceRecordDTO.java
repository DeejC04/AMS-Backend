package com.ams.restapi.attendance;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

public class AttendanceRecordDTO {
    private String room;
    private String date;
    private String time;
    private Long timestamp;
    private String sid;

    public AttendanceRecordDTO(AttendanceRecord record) {
        room = record.getRoom();
        date = record.getDate().toString();
        time = record.getTime().toString();
        sid = record.getSid();
    }

    public AttendanceRecord toEntity() {
        LocalDate rDate;
        LocalTime rTime;
        
        try {
            if (timestamp != null) {    
                LocalDateTime triggerTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamp),
                    ZoneId.of("MST", ZoneId.SHORT_IDS)); 
                rDate = triggerTime.toLocalDate();
                rTime = triggerTime.toLocalTime();
            } else {
                rDate = LocalDate.parse(date);
                rTime = LocalTime.parse(time);
            }

            // TODO: Implement type resolution logic

            return new AttendanceRecord(room, rDate, rTime, sid, null);
        } catch(DateTimeException e) {
            e.printStackTrace();
        }

        return null;
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
