package com.ams.restapi.courseInfo;

public class CourseInfoLogNotFoundException extends RuntimeException {
    CourseInfoLogNotFoundException(Long courseId) {
        super("Could not find courseInfo log " + courseId);
    }    
}