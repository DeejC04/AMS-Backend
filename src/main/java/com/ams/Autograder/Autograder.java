package com.ams.autograder;

import com.ams.restapi.attendance.AttendanceLog;
import com.ams.restapi.attendance.AttendanceRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.ksu.canvas.CanvasApiFactory;
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
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import io.github.cdimascio.dotenv.Dotenv;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;


@Service
public class Autograder {

    private final CanvasApiFactory apiFactory;
    private final OauthToken oauthToken;
    private final AttendanceRepository attendanceRepo;

    public Autograder(AttendanceRepository attendanceRepo){
        this.attendanceRepo = attendanceRepo;
        Dotenv env = Dotenv.load();
        apiFactory = new CanvasApiFactory(env.get("CANVAS_URL"));
        oauthToken = new NonRefreshableOauthToken(env.get("APIKEY"));
    }

    public void gradeAssignments() throws IOException{
        System.out.println(attendanceRepo.findById(1l));
        //This code iterates through every user in the course
        CourseReader courseReader = apiFactory.getReader(CourseReader.class, oauthToken);
        for(Course course : courseReader.listActiveCoursesInAccount(new ListActiveCoursesInAccountOptions("1"))){

            //creating an assignment for every student in the course
            Assignment attendanceAssignment = new Assignment();
            attendanceAssignment.setName("Attendance");
            attendanceAssignment.setGradingType("points");
            attendanceAssignment.setPointsPossible(5.0);
            attendanceAssignment.setPublished(true);

            //Using the Assignment writer we write the assignment to the canvas instance
            AssignmentWriter assnWriter = apiFactory.getWriter(AssignmentWriter.class, oauthToken);
            attendanceAssignment = assnWriter.createAssignment(course.getId().toString(), attendanceAssignment).get();            
            
            gradeAssignment(attendanceAssignment, course);

            // listing every user inside a course 
            EnrollmentReader enrlReader = apiFactory.getReader(EnrollmentReader.class, oauthToken);
            for(Enrollment enroll : enrlReader.getCourseEnrollments(new GetEnrollmentOptions(course.getId().toString()))){
                System.out.println(enroll.getUser().getName());
            }
        }
    }

    public void gradeAssignment(Assignment attendanceAssignment, Course course) throws IOException{
        SubmissionWriter submissionWriter = apiFactory.getWriter(SubmissionWriter.class, oauthToken);
        Map<String, MultipleSubmissionsOptions.StudentSubmissionOption> mapOfOptions = new HashMap<>();
        MultipleSubmissionsOptions submissionsOptions = new MultipleSubmissionsOptions(course.getId().toString(), attendanceAssignment.getId(), mapOfOptions);
        mapOfOptions.put("52", submissionsOptions.createStudentSubmissionOption("null", "5.0", false, false, "null", "null"));
        submissionsOptions.setStudentSubmissionOptionMap(mapOfOptions);
        submissionWriter.gradeMultipleSubmissionsByCourse(submissionsOptions);
        HashMap<String, List<AttendanceLog>> attendanceMap = new HashMap<>();
    }
}
