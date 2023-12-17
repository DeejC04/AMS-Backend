package com.ams.restapi.attendance;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AttendanceRecord {

    public static enum AttendanceType {
        ARRIVED, ARRIVED_LATE, ARRIVED_INVALID, LEFT_INVALID, LEFT, INVALID
    }

    private @Id @GeneratedValue Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String room;
    private LocalDate date;
    private LocalTime time;
    private String sid;
    private AttendanceType type; // I'll be the judge of this
    
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public AttendanceType getType() {
        return type;
    }

    public void setType(AttendanceType type) {
        this.type = type;
    }

    AttendanceRecord() {}

    public AttendanceRecord(String room, LocalDate date,
        LocalTime time, String sid, AttendanceType type) {
            this.room = room;
            this.date = date;
            this.time = time;
            this.sid = sid;
            this.type = type;
    }
    
    @Override
    public String toString() {
        return "AttendanceLog [id=" + id + ", room=" + room + ", date=" + date + ", time=" + time + ", sid=" + sid
                + ", type=" + type + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AttendanceRecord other = (AttendanceRecord) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
