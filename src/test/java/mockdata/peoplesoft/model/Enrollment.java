package mockdata.peoplesoft.model;

public record Enrollment(Person person, EnrollmentType type) {
    public enum EnrollmentType {
        INSTRUCTOR, TA, STUDENT
    }
}
