package com.ams.Autograder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.interfaces.AssignmentWriter;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.interfaces.EnrollmentReader;
import edu.ksu.canvas.interfaces.SubmissionWriter;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.model.Enrollment;
import edu.ksu.canvas.oauth.OauthToken;
import edu.ksu.canvas.requestOptions.GetEnrollmentOptions;
import edu.ksu.canvas.requestOptions.ListActiveCoursesInAccountOptions;
import edu.ksu.canvas.requestOptions.ListCourseAssignmentsOptions;
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import io.github.cdimascio.dotenv.Dotenv;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;



public class Autograder {

    private static final CanvasApiFactory apiFactory;
    private static final OauthToken oauthToken;
    static {
        Dotenv env = Dotenv.load();
        apiFactory = new CanvasApiFactory(env.get("CANVAS_URL"));
        oauthToken = new NonRefreshableOauthToken(env.get("APIKEY"));
    }

    public static void main(String[] args) throws IOException{
        
        CourseReader courseReader = apiFactory.getReader(CourseReader.class, oauthToken);
        for(Course course : courseReader.listActiveCoursesInAccount(new ListActiveCoursesInAccountOptions("1"))){

            //creating an assignment for every student in the course
            Assignment attendanceAssignment = new Assignment();
            attendanceAssignment.setName("Attendance");
            attendanceAssignment.setGradingType("points");
            attendanceAssignment.setPointsPossible(5.0);
            attendanceAssignment.setPublished(true);

            AssignmentWriter assnWriter = apiFactory.getWriter(AssignmentWriter.class, oauthToken);
            assnWriter.createAssignment(course.getId().toString(), attendanceAssignment);

            AssignmentReader assnReader = apiFactory.getReader(AssignmentReader.class, oauthToken);
            for(Assignment attendance : assnReader.listCourseAssignments(new ListCourseAssignmentsOptions(course.getId().toString()))){
                attendanceAssignment.setId(attendance.getId());
            }

            System.out.println(attendanceAssignment.getId());

            GradeAssignment(attendanceAssignment, course);

            // listing every user inside a course 
            EnrollmentReader enrlReader = apiFactory.getReader(EnrollmentReader.class, oauthToken);
            for(Enrollment enroll : enrlReader.getCourseEnrollments(new GetEnrollmentOptions(course.getId().toString()))){
                System.out.println(enroll.getUser().getName());
            }
        }
    }

    public static void GradeAssignment(Assignment attendanceAssignment, Course course) throws IOException{
        SubmissionWriter submissionWriter = apiFactory.getWriter(SubmissionWriter.class, oauthToken);
        Map<String, MultipleSubmissionsOptions.StudentSubmissionOption> mapOfOptions = new HashMap<>();

        MultipleSubmissionsOptions submissionsOptions = new MultipleSubmissionsOptions(course.getId().toString(), attendanceAssignment.getId(), mapOfOptions);
        mapOfOptions.put("52", submissionsOptions.createStudentSubmissionOption("null", "5.0", false, false, "null", "null"));
        submissionsOptions.setStudentSubmissionOptionMap(mapOfOptions);
        submissionWriter.gradeMultipleSubmissionsByCourse(submissionsOptions);
    }
}
