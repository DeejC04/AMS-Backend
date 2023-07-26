package com.ams.restapi.timeConfig;

import java.time.LocalDate;

public class DateSpecificTimeConfigNotFoundException extends RuntimeException {
    DateSpecificTimeConfigNotFoundException(Long courseID, LocalDate date) {
        super(String.format("Could not find time configuration log for %d (%s)",
            courseID, date.toString()));
    }
}
