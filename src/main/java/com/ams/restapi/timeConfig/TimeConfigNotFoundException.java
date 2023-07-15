package com.ams.restapi.timeConfig;

class TimeConfigNotFoundException extends RuntimeException {
    TimeConfigNotFoundException(Long id) {
        super("Could not find time configuration log " + id);
    }
}
