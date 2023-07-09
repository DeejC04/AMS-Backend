package com.ams.restapi.timeConfig;

class TimeConfigLogNotFoundException extends RuntimeException {
    TimeConfigLogNotFoundException(Long id) {
        super("Could not find time configuration log " + id);
    }
}
