package com.ams.restapi.attendance;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.ams.restapi.RestapiApplication;

import edu.ksu.canvas.enums.SectionIncludes;
import edu.ksu.canvas.model.Section;
import edu.ksu.canvas.model.User;

// @SpringBootTest
// @WebMvcTest
// @ContextConfiguration(classes = com.ams.restapi.RestapiApplication.class)
public class SectionReadTest {
    // private final AttendanceController controller;
    
    // public SectionReadTest(AttendanceController controller) {
    //     this.controller = controller;
    // }

    // @Test public void sectionTest() throws IOException {
    //     List<Section> sections = controller.sections.listCourseSections("1",
    //         List.of(SectionIncludes.STUDENTS));
    //     for (Section section : sections) {
    //         System.out.println(section.getName());
    //         for (User user : section.getStudents()) {
    //             System.out.printf("%s (%s)%n", user.getName(), user.getSisUserId());
    //         }
    //     }
    // }
}
