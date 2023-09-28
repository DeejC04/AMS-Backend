package mockdata.peoplesoft.model;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;

public record Section(
    Course course,
    Integer id,
    List<DayOfWeek> days,
    LocalTime startTime,
    LocalTime endTime,
    Location location,
    LocalDate startDate,
    LocalDate endDate,
    Integer occupiedSeats,
    Integer totalSeats,
    List<Enrollment> enrollments
) {
    public Section(
        Course course,
        Integer id,
        List<DayOfWeek> days,
        LocalTime startTime,
        LocalTime endTime,
        Location location,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalSeats,
        List<Enrollment> enrollments
    ) {
        this(course, id,
            days, startTime, endTime, location, startDate,
            endDate, 0, totalSeats, enrollments);
    }
}
