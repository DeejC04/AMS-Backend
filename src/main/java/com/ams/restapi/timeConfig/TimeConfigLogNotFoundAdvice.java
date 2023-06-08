package com.ams.restapi.timeConfig;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class TimeConfigLogNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(TimeConfigLogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String TimeConfigLogNotFoundHandler(TimeConfigLogNotFoundException ex) {
        return ex.getMessage();
    }
}
