package com.ams.restapi.attendance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class AttendanceLogNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(AttendanceLogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String attendanceLogNotFoundHandler(AttendanceLogNotFoundException ex) {
        return ex.getMessage();
    }
}
