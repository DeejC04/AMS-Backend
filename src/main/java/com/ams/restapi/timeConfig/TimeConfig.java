package com.ams.restapi.timeConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class TimeConfig {

    private @Id @GeneratedValue Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Integer date;

    public Integer getDate() {
        return date;
    }

    public void setId(Integer date) {
        this.date = date;
    }

    private long courseID;
    private String begin_in, end_in, end_late, begin_out, end_out;

    public TimeConfig(long courseID, String begin_in, String end_in, String end_late, String begin_out,
            String end_out) {
        this.courseID = courseID;
        this.begin_in = begin_in;
        this.begin_out = begin_out;
        this.end_in = end_in;
        this.end_out = end_out;
        this.end_late = end_late;

    }

    public TimeConfig() {

    }

    @Override
    public String toString() {
        return "Course #" + courseID + " has the following configuration:"
                + "\nBegin_in: " + begin_in
                + "\nbegin_out: " + begin_out
                + "\nend_in: " + end_in
                + "\nend_out " + end_out
                + "\nend_late: " + end_late + "\n";
    }

    public String getEnd_out() {
        return end_out;
    }

    public void setEnd_out(String end_out) {
        this.end_out = end_out;
    }

    public String getBegin_out() {
        return begin_out;
    }

    public void setBegin_out(String begin_out) {
        this.begin_out = begin_out;
    }

    public String getEnd_late() {
        return end_late;
    }

    public void setEnd_late(String end_late) {
        this.end_late = end_late;
    }

    public String getEnd_in() {
        return end_in;
    }

    public void setEnd_in(String end_in) {
        this.end_in = end_in;
    }

    public String getBegin_in() {
        return begin_in;
    }

    public void setBegin_in(String begin_in) {
        this.begin_in = begin_in;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }
}