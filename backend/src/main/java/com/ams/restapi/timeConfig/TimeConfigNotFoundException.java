package com.ams.restapi.timeConfig;

class TimeConfigNotFoundException extends RuntimeException {
    TimeConfigNotFoundException(Long courseID) {
        super("Could not find time configuration log " + courseID);
    }
}
