package com.ams.restapi.timeConfig;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TimeConfig {

    private @Id Long courseID;
    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    private LocalTime beginIn;
    private LocalTime endIn;
    private LocalTime endLate;
    private LocalTime beginOut;
    private LocalTime endOut;

    public TimeConfig(LocalTime beginIn, LocalTime endIn,
            LocalTime endLate, LocalTime beginOut, LocalTime endOut) {
        this.beginIn = beginIn;
        this.endIn = endIn;
        this.endLate = endLate;
        this.beginOut = beginOut;
        this.endOut = endOut;
    }

    public TimeConfig() {}

    public LocalTime getEndOut() {
        return endOut;
    }

    public void setEndOut(LocalTime endOut) {
        this.endOut = endOut;
    }

    public LocalTime getBeginOut() {
        return beginOut;
    }

    public void setBeginOut(LocalTime beginOut) {
        this.beginOut = beginOut;
    }

    public LocalTime getEndLate() {
        return endLate;
    }

    public void setEndLate(LocalTime endLate) {
        this.endLate = endLate;
    }

    public LocalTime getEndIn() {
        return endIn;
    }

    public void setEndIn(LocalTime endIn) {
        this.endIn = endIn;
    }

    public LocalTime getBeginIn() {
        return beginIn;
    }

    public void setBeginIn(LocalTime beginIn) {
        this.beginIn = beginIn;
    }

    @Override
    public String toString() {
        return "TimeConfig [courseID=" + courseID + ", beginIn=" + beginIn + ", endIn=" + endIn + ", endLate=" + endLate
                + ", beginOut=" + beginOut + ", endOut=" + endOut + "]";
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