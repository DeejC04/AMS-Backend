package com.ams.restapi.attendance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class AttendanceControllerAdvice {
    @ResponseBody
    @ExceptionHandler({AttendanceLogNotFoundException.class, AttendanceLogPageOutofBoundsException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String attendanceHandler(AttendanceLogNotFoundException ex) {
        return ex.getMessage();
    }
}
