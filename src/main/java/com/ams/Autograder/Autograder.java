package com.ams.Autograder;

import com.ams.restapi.attendance.AttendanceLog;
import com.ams.restapi.attendance.AttendanceRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.enums.SectionIncludes;
import edu.ksu.canvas.interfaces.AssignmentWriter;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.interfaces.EnrollmentReader;
import edu.ksu.canvas.interfaces.SectionReader;
import edu.ksu.canvas.interfaces.SubmissionWriter;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.model.Enrollment;
import edu.ksu.canvas.model.Section;
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

    Autograder(AttendanceRepository attendanceRepo){
        this.attendanceRepo = attendanceRepo;
        Dotenv env = Dotenv.load();
        apiFactory = new CanvasApiFactory(env.get("CANVAS_URL"));
        oauthToken = new NonRefreshableOauthToken(env.get("APIKEY"));
    }

    public void gradeAssignments() throws IOException{
        //This code iterates through every user in the course
        CourseReader courseReader = apiFactory.getReader(CourseReader.class, oauthToken);
        for(Course course : courseReader.listActiveCoursesInAccount(new ListActiveCoursesInAccountOptions("1"))){
            //creating an assignment for every student in the course
            Assignment attendanceAssignment = new Assignment();
            attendanceAssignment.setName("Attendance");
            attendanceAssignment.setGradingType("points");
            attendanceAssignment.setPointsPossible(2.0);
            attendanceAssignment.setPublished(true);

            //Using the Assignment writer we write the assignment to the canvas instance
            AssignmentWriter assnWriter = apiFactory.getWriter(AssignmentWriter.class, oauthToken);
            attendanceAssignment = assnWriter.createAssignment(course.getId().toString(), attendanceAssignment).get(); 

            SectionReader sectReader = apiFactory.getReader(SectionReader.class, oauthToken);           
            for(Section section : sectReader.listCourseSections(course.getId().toString(), new ArrayList<SectionIncludes>())){
                        
                EnrollmentReader enrlReader = apiFactory.getReader(EnrollmentReader.class, oauthToken);
                for(Enrollment enroll : enrlReader.getCourseEnrollments(new GetEnrollmentOptions(course.getId().toString()))){
                    
                    String sisId = enroll.getUser().getSisUserId();
                    Long userId = enroll.getUser().getId();
                    int fullPoints = 0;
                    for(AttendanceLog attendanceLog : attendanceRepo.findBySid(sisId)){
                        if(attendanceLog.getType().equals("ARRIVED") || attendanceLog.getType().equals("LEFT"))
                            fullPoints += 1;
                        else if(attendanceLog.getType().equals("ARRIVED_INVALID")){
                            fullPoints = 0;
                            break;
                        }
                        else if(attendanceLog.getType().equals("ARRIVED_LATE")){
                            fullPoints = 1;
                            break;
                        }
                    }
                    gradeAssignment(attendanceAssignment, section, userId.toString(), fullPoints);
                }
            }
        }
    }

    public void gradeAssignment(Assignment attendanceAssignment, Section section, String userId, int fullPoints) throws IOException{
        SubmissionWriter submissionWriter = apiFactory.getWriter(SubmissionWriter.class, oauthToken);
        Map<String, MultipleSubmissionsOptions.StudentSubmissionOption> mapOfOptions = new HashMap<>();
        MultipleSubmissionsOptions submissionsOptions = new MultipleSubmissionsOptions(section.getId().toString(), attendanceAssignment.getId(), mapOfOptions);
        mapOfOptions.put(userId, submissionsOptions.createStudentSubmissionOption("null", String.valueOf(fullPoints), false, false, "null", "null"));
        submissionsOptions.setStudentSubmissionOptionMap(mapOfOptions);
        submissionWriter.gradeMultipleSubmissionsBySection(submissionsOptions);
        HashMap<String, List<AttendanceLog>> attendanceMap = new HashMap<>();
    }
}
