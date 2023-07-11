package com.ams.restapi.attendance;

public class AttendanceLogPageOutofBoundsException extends RuntimeException {
    AttendanceLogPageOutofBoundsException(Integer page, Integer size) {
        super(String.format("Could not retrieve page %d of size %d", page, size));
    }
}
