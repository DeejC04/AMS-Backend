package com.ams.autograder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.ams.autograder.Autograder;
import com.ams.restapi.attendance.AttendanceRecord;
import com.ams.restapi.attendance.AttendanceRepository;
import com.ams.restapi.courseInfo.CourseInfo;
import com.ams.restapi.courseInfo.CourseInfoRepository;

@DataJpaTest
@ContextConfiguration(classes = com.ams.restapi.RestapiApplication.class)
public class AutograderTests {
    private final AttendanceRepository attendanceRepo;
    private final Autograder autograder; 

    @Autowired
    public AutograderTests(AttendanceRepository attendanceRepo, CourseInfoRepository courseInfo) throws IOException {
        this.attendanceRepo = attendanceRepo;
        autograder = new Autograder(attendanceRepo);
        
        BufferedReader reader = new BufferedReader(new FileReader("./mock.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\\s*,\\s*");
            System.out.println("Preloading " + attendanceRepo.save(
                new AttendanceRecord(tokens[0], LocalDate.parse(tokens[1]), LocalTime.parse(tokens[2]),
                    tokens[3], AttendanceRecord.AttendanceType.valueOf(tokens[4]))));
        }
        reader.close();

        courseInfo.save(
                    new CourseInfo(
                        85L, 77L, "CSE110", "COOR170",
                        List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                        LocalTime.of(12, 15), LocalTime.of(13,  5))
                );
    }

    // @Test
	// void TestAssignmentGrader() throws IOException {        
    //     for (AttendanceRecord log : attendanceRepo.findAll()) {
    //         System.out.println(log);
    //     }
    //     autograder.gradeAssignments();
	// }
}
