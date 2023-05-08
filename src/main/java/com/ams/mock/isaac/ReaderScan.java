package com.ams.mock.isaac;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ReaderScan {
    private @Id @GeneratedValue Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    private String room;
    private Long time;
    private String sid;

    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }

    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }

    ReaderScan() {
        
    }
    
    @Override
    public String toString() {
        return "ReaderScan [id=" + id + ", room=" + room + ", time=" + time + ", sid=" + sid + "]";
    }

    
}
