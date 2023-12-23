package com.ams.restapi.courseInfo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.ams.restapi.timeConfig.DateSpecificTimeConfig;
// import com.ams.restapi.timeConfig.DateSpecificTimeConfig;
import com.ams.restapi.timeConfig.TimeConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class CourseInfo {

    private static Long DEFAULT_TOLERANCE = 5L;
    private static Long DEFAULT_LATE_TOLERANCE = 15L;

    private @Id Long id;
    private Long courseId;
    private String name, room;
    // @CollectionTable(joinColumns = @JoinColumn(name = "courseinfo_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    private List<DayOfWeek> daysOfWeek;
    private LocalTime startTime, endTime;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    private TimeConfig defaultTimeConfig;

    public TimeConfig getDefaultTimeConfig() {
        return defaultTimeConfig;
    }

    public void setDefaultTimeConfig(TimeConfig defaultTimeConfig) {
        this.defaultTimeConfig = defaultTimeConfig;
    }

    @JsonIgnore    
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<DateSpecificTimeConfig> dateSpecificTimeConfigs;

    public List<DateSpecificTimeConfig> getDateSpecificTimeConfigs() {
        return dateSpecificTimeConfigs;
    }

    public void setDateSpecificTimeConfigs(List<DateSpecificTimeConfig> dateSpecificTimeConfigs) {
        this.dateSpecificTimeConfigs = dateSpecificTimeConfigs;
    }

    public CourseInfo() {}

    public CourseInfo(Long id, Long courseId, String name, String room,
            List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
        defaultTimeConfig = getDefaultTimeConfig(this, startTime, endTime);
        dateSpecificTimeConfigs = new ArrayList<>();
    }
    
    public CourseInfo(Long id, String name, String room,
            List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime,
            TimeConfig defaultTimeConfig) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
        this.defaultTimeConfig = defaultTimeConfig;
        dateSpecificTimeConfigs = new ArrayList<>();
    }

    public static TimeConfig getDefaultTimeConfig(CourseInfo course, LocalTime startTime, LocalTime endTime) {
        TimeConfig config = new TimeConfig(course,
            startTime.minusMinutes(DEFAULT_TOLERANCE),
            startTime.plusMinutes(DEFAULT_TOLERANCE),
            startTime.plusMinutes(DEFAULT_LATE_TOLERANCE),
            endTime.minusMinutes(DEFAULT_TOLERANCE),
            endTime.plusMinutes(DEFAULT_TOLERANCE));

        if (course.getDefaultTimeConfig() != null) config.setId(course.getDefaultTimeConfig().getId());

        return config;
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

    public String getName() {
        return name;
    }

    public void setName(String courseName) {
        this.name = courseName;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public String toString() {
        return "CourseInfo [id=" + id + ", room=" + room + ", courseName=" + name + ", daysOfWeek="
                + daysOfWeek + ", startTime=" + startTime + ", endTime=" + endTime + "]";
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
        CourseInfo other = (CourseInfo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

    
}
