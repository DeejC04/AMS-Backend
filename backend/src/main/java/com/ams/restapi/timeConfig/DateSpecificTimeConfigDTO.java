package com.ams.restapi.timeConfig;

import java.time.LocalDate;

public class DateSpecificTimeConfigDTO {
    private TimeConfigDTO timeConfig;
    private LocalDate date;

    public DateSpecificTimeConfigDTO() {}
    
    public DateSpecificTimeConfigDTO(DateSpecificTimeConfig config) {
        timeConfig = new TimeConfigDTO(config.getConfig());
        date = config.getDate();
    }
    
    public TimeConfigDTO getTimeConfig() {
        return timeConfig;
    }
    public void setTimeConfig(TimeConfigDTO timeConfig) {
        this.timeConfig = timeConfig;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
}
