package com.ams.autograder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.AssignmentWriter;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.interfaces.EnrollmentReader;
import edu.ksu.canvas.interfaces.SubmissionWriter;
import edu.ksu.canvas.model.Enrollment;
import edu.ksu.canvas.oauth.OauthToken;
import edu.ksu.canvas.requestOptions.GetEnrollmentOptions;
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;

public class Autograder {

    public static void main(String[] args) throws IOException{
        //creating a connection to a canvas instance
        String canvasBaseUrl = "http://104.237.159.242:3000/";
        OauthToken oauthToken = new NonRefreshableOauthToken("");
        CanvasApiFactory apiFactory = new CanvasApiFactory(canvasBaseUrl);

        //creating an assignment for every student in the course
        Assignment attendanceAssignment = new Assignment();
        attendanceAssignment.setName("Attendance");
        attendanceAssignment.setGradingType("points");
        attendanceAssignment.setPointsPossible(5.0);
        attendanceAssignment.setPublished(true);
        attendanceAssignment.setUnpublishable(false);
        attendanceAssignment.setId(22L);
    
        List<String> list = new ArrayList<String>();
        list.add("on_paper");
        attendanceAssignment.setSubmissionTypes(list);

        AssignmentWriter assnWriter = apiFactory.getWriter(AssignmentWriter.class, oauthToken);
        assnWriter.createAssignment("1", attendanceAssignment);

        SubmissionWriter submissionWriter = apiFactory.getWriter(SubmissionWriter.class, oauthToken);
        Map<String, MultipleSubmissionsOptions.StudentSubmissionOption> mapOfOptions = new HashMap<>();
        
        MultipleSubmissionsOptions submissionsOptions = new MultipleSubmissionsOptions("1", 22L, null);
        mapOfOptions.put("7", submissionsOptions.createStudentSubmissionOption("null", "5.0", false, false, "null", "null"));
        submissionsOptions.setStudentSubmissionOptionMap(mapOfOptions);
        submissionWriter.gradeMultipleSubmissionsByCourse(submissionsOptions);

        // HashMap<String, List<AttendanceLog>> attendanceMap = new HashMap<>();
        

        //listing every user inside a course 
        EnrollmentReader enrlReader = apiFactory.getReader(EnrollmentReader.class, oauthToken);
        for(Enrollment enroll : enrlReader.getCourseEnrollments(new GetEnrollmentOptions("1"))){
            System.out.println(enroll.getUser().getName());
        }
    }
}
