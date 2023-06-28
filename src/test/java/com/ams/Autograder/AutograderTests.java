package com.ams.Autograder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.ams.restapi.attendance.AttendanceLog;
import com.ams.restapi.attendance.AttendanceRepository;

@DataJpaTest
@ContextConfiguration(classes = com.ams.restapi.RestapiApplication.class)
public class AutograderTests {
    private final AttendanceRepository attendanceRepo;
    private final Autograder autograder; 

    @Autowired
    public AutograderTests(AttendanceRepository attendanceRepo) throws IOException {
        this.attendanceRepo = attendanceRepo;
        autograder = new Autograder(attendanceRepo);
        
        BufferedReader reader = new BufferedReader(new FileReader("./mock.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\\s*,\\s*");
            System.out.println("Preloading " + attendanceRepo.save(
                new AttendanceLog(tokens[0], Long.parseLong(tokens[1]), tokens[2], tokens[3])));
        }
        reader.close();
    }

    @Test
	void TestAssignmentGrader() throws IOException {        
        for (AttendanceLog log : attendanceRepo.findAll()) {
            System.out.println(log);
        }
        autograder.gradeAssignments();
	}
}
