package com.ams.restapi.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class WiegandData {
    private @Id @GeneratedValue Long id;
    private String time;
    private String data;
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "WiegandData [time=" + time + ", data=" + data + "]";
    }
}
