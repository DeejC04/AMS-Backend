package com.ams.restapi.courseInfo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import com.ams.restapi.timeConfig.TimeConfigDTO;

public class CourseInfoDTO {
    private String name, room;
    private List<DayOfWeek> daysOfWeek;
    private LocalTime startTime, endTime;
    private TimeConfigDTO defaultTimeConfig;
    
    public TimeConfigDTO getDefaultTimeConfig() {
        return defaultTimeConfig;
    }

    public void setDefaultTimeConfig(TimeConfigDTO defaultTimeConfig) {
        this.defaultTimeConfig = defaultTimeConfig;
    }

    public CourseInfoDTO() {}

    public CourseInfoDTO(CourseInfo courseInfo) {
        name = courseInfo.getName();
        room = courseInfo.getRoom();
        startTime = courseInfo.getStartTime();
        endTime = courseInfo.getEndTime();
        daysOfWeek = courseInfo.getDaysOfWeek();
        defaultTimeConfig = new TimeConfigDTO(courseInfo.getDefaultTimeConfig());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public CourseInfo toEntity(Long courseID) {
        CourseInfo course = new CourseInfo(courseID, name, room,
            daysOfWeek, startTime, endTime, null);
        if (defaultTimeConfig == null)
            course.setDefaultTimeConfig(
                CourseInfo.getDefaultTimeConfig(course, startTime, endTime));
        else
            course.setDefaultTimeConfig(defaultTimeConfig.toEntity(course));
        return course;
    }
    
}
