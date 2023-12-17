package com.ams.restapi.timeConfig;

import java.time.LocalTime;

import com.ams.restapi.courseInfo.CourseInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class TimeConfig {

    private @Id @GeneratedValue Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private LocalTime beginIn;
    private LocalTime endIn;
    private LocalTime endLate;
    private LocalTime beginOut;
    private LocalTime endOut;

    @JsonBackReference
    @OneToOne(mappedBy = "defaultTimeConfig")
    private CourseInfo course;
    
    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    public TimeConfig() {}

    public TimeConfig(CourseInfo course, LocalTime beginIn, LocalTime endIn,
            LocalTime endLate, LocalTime beginOut, LocalTime endOut) {
        this.course = course;
        this.beginIn = beginIn;
        this.endIn = endIn;
        this.endLate = endLate;
        this.beginOut = beginOut;
        this.endOut = endOut;
    }

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
        return "TimeConfig [id=" + id + ", beginIn=" + beginIn + ", endIn=" + endIn + ", endLate=" + endLate
                + ", beginOut=" + beginOut + ", endOut=" + endOut + ", course=" + course + "]";
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
        TimeConfig other = (TimeConfig) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
