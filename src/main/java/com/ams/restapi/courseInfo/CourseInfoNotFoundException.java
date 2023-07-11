package com.ams.restapi.courseInfo;

public class CourseInfoNotFoundException extends RuntimeException {
    CourseInfoNotFoundException(Long courseId) {
        super("Could not find courseInfo log " + courseId);
    }    
}