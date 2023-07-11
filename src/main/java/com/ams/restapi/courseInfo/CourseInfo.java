package com.ams.restapi.courseInfo;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.DayOfWeek;

@Entity
public class CourseInfo {

    private @Id Long courseId;
    private String room, courseName;
    private List<DayOfWeek> daysOfWeek;
    private Long startTime, endTime;

    public CourseInfo() {

    }

    public CourseInfo(String room, Long startTime, Long endTime, String courseName,
        List<DayOfWeek> daysOfWeek, Long courseId) {
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseName = courseName;
        this.daysOfWeek = daysOfWeek;
        this.courseId = courseId;
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
        return "CourseInfo [courseId=" + courseId + ", room=" + room + ", courseName=" + courseName + ", daysOfWeek="
                + daysOfWeek + ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        CourseInfo other = (CourseInfo) obj;
        if (courseId == null) {
            if (other.courseId != null)
                return false;
        } else if (!courseId.equals(other.courseId))
            return false;
        return true;
    }

    
}
