package mockdata.peoplesoft;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalTime;

import mockdata.ams.Repository;
import mockdata.ams.SectionInfoRepo;
import mockdata.peoplesoft.model.Course;
import mockdata.peoplesoft.model.Enrollment;
import mockdata.peoplesoft.model.Location;
import mockdata.peoplesoft.model.Role;
import mockdata.peoplesoft.model.Section;
import mockdata.peoplesoft.model.Enrollment.EnrollmentType;

public class SectionGenerator implements Generator<Section> {

    private static Random rand = new Random();
    
    private static Integer sectionID = 0;
    
    private SectionInfoRepo repo;
    
    public SectionGenerator(SectionInfoRepo repo) {
        this.repo = repo;
    }

    public boolean generate(Course course, int numSections,
            int sectionSize, UserGenerator userGenerator) {
        for (int i = 0; i < numSections; i++) {

            List<Enrollment> enrollments = new ArrayList<Enrollment>();
            
            // create instructor
            enrollments.add(new Enrollment(
                userGenerator.nextPerson(Role.FACULTY, "Teaching Professor"), EnrollmentType.INSTRUCTOR));

            // create students
            for (int j = 0; j < sectionSize; j++) {
                enrollments.add(new Enrollment(
                    userGenerator.nextPerson(Role.STUDENT, "Undergraduate Student"), EnrollmentType.STUDENT));
            }

            List<DayOfWeek> weekdays = generateWeekdays();
            List<LocalTime> times = generateTimes(weekdays);
            List<LocalDate> sessionDates = generateSessionDates();
            repo.add(new Section(
                course, sectionID++, weekdays,
                times.get(0), times.get(1),
                generateRandomLocation(),
                sessionDates.get(0), sessionDates.get(1),
                generateRandomSeats(sectionSize), sectionSize, enrollments));
        }
        return false;
    }

    @Override
    public Repository<Section> getRepository() {
        return repo;
    }

    private List<DayOfWeek> generateWeekdays() {
        List<List<DayOfWeek>> schedules = List.of(
            List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        );
        return schedules.get(rand.nextInt(schedules.size()));
    }

    private List<LocalTime> generateTimes(List<DayOfWeek> weekdays) {
        // start time
        int hour = rand.nextInt(9) + 7;
        int min = rand.nextInt(4) * 15;
        LocalTime startTime = LocalTime.of(hour, min);

        // end time
        int duration = weekdays.containsAll(
            List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))
            ? 50 : 75;
        LocalTime endTime = startTime.plusMinutes(duration);
        
        return List.of(startTime, endTime);
    }

    private Integer generateRandomSeats(int totalSeats) {
        return rand.nextInt(totalSeats) + 1;
    }

    private Character generateSessionType() {
        double randomValue = rand.nextDouble();
        if (randomValue < .1)
            return 'A';
        else if (randomValue < .2)
            return 'B';
        else
            return 'C';
    }

    private List<LocalDate> generateSessionDates() {
        LocalDate semesterStartDate = LocalDate.of(2023, 8, 20);
        LocalDate semesterEndDate = LocalDate.of(2023, 12, 8);

        int numDays = (int)(Duration.between(
            semesterStartDate.atStartOfDay(), semesterEndDate.atStartOfDay()).toDays() + 1);
        
        LocalDate sessionAEndDate = semesterStartDate.plusDays(numDays / 2);
        LocalDate sessionBStartDate = sessionAEndDate.plusDays(1);
        
        return switch (generateSessionType()) {
            case 'A' -> List.of(semesterStartDate, sessionAEndDate);
            case 'B' -> List.of(sessionBStartDate, semesterEndDate);
            default -> List.of(semesterStartDate, semesterEndDate);
        };
    }

    private Location generateRandomLocation() {
        List<String> campuses = List.of("Tempe", "Downtown", "Polytechnic");
        List<String> rooms = List.of("COORL1-74", "WXLRA304", "PSH150", "PICHO150");
    
        return new Location(
            campuses.get(rand.nextInt(campuses.size())),
            rooms.get(rand.nextInt(rooms.size())));
    }
}
