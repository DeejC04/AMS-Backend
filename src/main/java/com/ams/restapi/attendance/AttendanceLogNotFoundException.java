package com.ams.restapi.attendance;

class AttendanceLogNotFoundException extends RuntimeException {
    AttendanceLogNotFoundException(Long id) {
        super("Could not find attendance log " + id);
    }    
}
