package com.ams.restapi.timeConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
class TimeConfig {

    private @Id Long courseID;
    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    private String beginIn;
    private String endIn;
    private String endLate;
    private String beginOut;
    private String endOut;

    public TimeConfig(String beginIn, String endIn, String endLate, String beginOut,
            String endOut) {
        this.beginIn = beginIn;
        this.endIn = endIn;
        this.endLate = endLate;
        this.beginOut = beginOut;
        this.endOut = endOut;
    }

    public TimeConfig() {

    }

    @Override
    public String toString() {
        return "Course #" + courseID + " has the following configuration:"
                + "\nBegin_in: " + beginIn
                + "\nbegin_out: " + beginOut
                + "\nend_in: " + endIn
                + "\nend_out " + endOut
                + "\nend_late: " + endLate + "\n";
    }

    public String getEndOut() {
        return endOut;
    }

    public void setEndOut(String end_out) {
        this.endOut = end_out;
    }

    public String getBeginOut() {
        return beginOut;
    }

    public void setBeginOut(String begin_out) {
        this.beginOut = begin_out;
    }

    public String getEndLate() {
        return endLate;
    }

    public void setEndLate(String end_late) {
        this.endLate = end_late;
    }

    public String getEndIn() {
        return endIn;
    }

    public void setEndIn(String end_in) {
        this.endIn = end_in;
    }

    public String getBeginIn() {
        return beginIn;
    }

    public void setBeginIn(String begin_in) {
        this.beginIn = begin_in;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseID == null) ? 0 : courseID.hashCode());
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
        TimeConfig other = (TimeConfig) obj;
        if (courseID == null) {
            if (other.courseID != null)
                return false;
        } else if (!courseID.equals(other.courseID))
            return false;
        return true;
    }

    
}