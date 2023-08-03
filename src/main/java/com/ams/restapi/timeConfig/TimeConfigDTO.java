package com.ams.restapi.timeConfig;

import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import com.ams.restapi.courseInfo.CourseInfo;

public class TimeConfigDTO {
    private Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull(message = "beginIn cannot be missing or empty")
    private LocalTime beginIn;

    @NotNull(message = "endIn cannot be missing or empty")
    private LocalTime endIn;

    @NotNull(message = "endLate cannot be missing or empty")
    private LocalTime endLate;

    @NotNull(message = "beginOut cannot be missing or empty")
    private LocalTime beginOut;

    @NotNull(message = "endOut cannot be missing or empty")
    private LocalTime endOut;

    public TimeConfigDTO() {}
    
    public TimeConfigDTO(TimeConfig timeConfig) {
        id = timeConfig.getId();
        beginIn = timeConfig.getBeginIn();
        endIn = timeConfig.getEndIn();
        endLate = timeConfig.getEndLate();
        beginOut = timeConfig.getBeginOut();
        endOut = timeConfig.getEndOut();
    }

    public LocalTime getBeginIn() {
        return beginIn;
    }

    public void setBeginIn(LocalTime beginIn) {
        this.beginIn = beginIn;
    }

    public LocalTime getEndIn() {
        return endIn;
    }

    public void setEndIn(LocalTime endIn) {
        this.endIn = endIn;
    }

    public LocalTime getEndLate() {
        return endLate;
    }

    public void setEndLate(LocalTime endLate) {
        this.endLate = endLate;
    }

    public LocalTime getBeginOut() {
        return beginOut;
    }

    public void setBeginOut(LocalTime beginOut) {
        this.beginOut = beginOut;
    }

    public LocalTime getEndOut() {
        return endOut;
    }

    public void setEndOut(LocalTime endOut) {
        this.endOut = endOut;
    }

    public TimeConfig toEntity(CourseInfo course) {
        return new TimeConfig(course, beginIn, endIn,
            endLate, beginOut, endOut);
    }
    
}
