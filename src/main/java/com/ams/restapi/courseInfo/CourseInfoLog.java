package com.ams.restapi.courseInfo;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.DayOfWeek;

@Entity
public class CourseInfoLog {

    private @Id @GeneratedValue Long id;
    private String room, courseName;
    private List<DayOfWeek> daysOfWeek;
    private Long startTime, endTime, courseId;

    public CourseInfoLog() {
    }

    public CourseInfoLog(String room, long startTime, long endTime, String courseName, List<DayOfWeek> daysOfWeek, long courseId) {
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseName = courseName;
        this.daysOfWeek = daysOfWeek;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public String toString() {
        return "CourseInfoLog [id=" + id + ", courseId=" + courseId + ", room=" + room + ", courseName=" + courseName + ", startTime="
                + startTime
                + ", endTime=" + endTime + ", daysOfWeek=" + daysOfWeek + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
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

        CourseInfoLog other = (CourseInfoLog) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;

        if (courseId == null) {
            if (other.courseId != null)
                return false;
        } else if (!courseId.equals(other.courseId))
            return false;

        return true;
    }
}
