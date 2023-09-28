package mockdata.peoplesoft;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import mockdata.ams.AttendanceRepo;
import mockdata.ams.CourseInfoRepo;
import mockdata.ams.SectionInfoRepo;
import mockdata.peoplesoft.AttendanceGenerator.AttendanceTypeParam;
import mockdata.peoplesoft.model.Course;
import mockdata.peoplesoft.model.Section;

public class MockGenerator {
    public static void main(String[] args) {
    }

    protected static void runMock(LocalDate initDate, int numDays, CourseInfoRepo courses,
            int numSections, int sectionSize) {

        SectionInfoRepo sectionRepo = new SectionInfoRepo();
        SectionGenerator sectionGenerator = new SectionGenerator(sectionRepo);
        UserGenerator userGenerator = new UserGenerator();
        for (Course course : courses) {
            sectionGenerator.generate(course, numSections, sectionSize, userGenerator);
        }

        AttendanceRepo attendanceRepo = new AttendanceRepo();
        AttendanceGenerator attendanceGenerator = new AttendanceGenerator(attendanceRepo);
        for (Section section : sectionRepo) {
            // TODO: Switch out with values from TimeConfigGenerator
            int TOLERANCE = 5;
            int LATE_TOLERANCE = 15;
            LocalTime beginInTolerance = section.startTime().minusMinutes(TOLERANCE);
            LocalTime endInTolerance = section.startTime().plusMinutes(TOLERANCE);

            LocalTime endLateTolerance = section.startTime().plusMinutes(LATE_TOLERANCE);

            LocalTime beginOutTolerance = section.endTime().minusMinutes(TOLERANCE);
            LocalTime endOutTolerance = section.endTime().plusMinutes(TOLERANCE);

            // PRESENT, LATE, SUPERLATE, LEFTEARLY, LEFT, LEFTLATE

            // scans before the beginning of in tolerance and after the end of out tolerance
            // are ignored (assigned invalid)
            List<AttendanceTypeParam> types = new ArrayList<>(List.of(
                    new AttendanceTypeParam("PRESENT",
                            75, beginInTolerance, endInTolerance,
                            beginOutTolerance, endOutTolerance),
                    new AttendanceTypeParam("LATE",
                            10, endInTolerance, endLateTolerance,
                            beginOutTolerance, endOutTolerance),
                    new AttendanceTypeParam("SUPERLATE",
                            5, endLateTolerance, beginOutTolerance,
                            beginOutTolerance, endOutTolerance),
                    new AttendanceTypeParam("SUPERLATE_LEFTEARLY",
                            5, endLateTolerance, beginOutTolerance,
                            endLateTolerance, beginOutTolerance), // in/out may end up getting swapped around, doesn't
                                                                  // really matter
                    new AttendanceTypeParam("LEFTEARLY",
                            5, beginInTolerance, endInTolerance,
                            endInTolerance, beginOutTolerance)));

            attendanceGenerator.generate(section, initDate, numDays, types);
        }

    }
}
