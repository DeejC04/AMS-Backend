package com.ams.restapi.courseInfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CourseInfoLogNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CourseInfoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String courseInfoLogNotFoundHandler(CourseInfoNotFoundException ex) {
        return ex.getMessage();
    }
}
