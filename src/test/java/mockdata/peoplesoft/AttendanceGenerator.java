package mockdata.peoplesoft;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import mockdata.ams.AttendanceRecord;
import mockdata.ams.AttendanceRecord.AttendanceRecordType;
import mockdata.ams.AttendanceRepo;
import mockdata.ams.Repository;
import mockdata.peoplesoft.model.Enrollment;
import mockdata.peoplesoft.model.Person;
import mockdata.peoplesoft.model.Section;
import mockdata.peoplesoft.model.Enrollment.EnrollmentType;

public class AttendanceGenerator implements Generator<AttendanceRecord> {

    // TODO: Generate these tolerance values?
    private static final int TOLERANCE = 5; // minutes
    private static final int LATE_TOLERANCE = 15; // minutes

    // private static final int PERC_PRESENT = 60;
    // private static final int PERC_LATE = 30;
    // private static final int PERC_LATE_INVALID = 10;

    private static final Random random = new Random();

    private AttendanceRepo repo;

    public AttendanceGenerator(AttendanceRepo repo) {
        this.repo = repo;
    }

    protected static record AttendanceTypeParam(
        String name,
        int percent,
        LocalTime beginIn,
        LocalTime endIn,
        LocalTime beginOut,
        LocalTime endOut
    ) {}

    private static class AttendanceTypeParamComparator implements Comparator<AttendanceTypeParam> {
        @Override
        public int compare(AttendanceTypeParam a1, AttendanceTypeParam a2) {
            return a1.percent() - a2.percent();
        }
    }

    public void generate(Section section, LocalDate initDate, Integer numDays,
            List<AttendanceTypeParam> types) {

        List<LocalDate> days = new ArrayList<>(numDays);
        days.add(0, initDate);
        for (int i = 1; i < numDays; i++) {
            days.add(i, days.get(i - 1).plusDays(1));
        }

        HashMap<LocalTime, List<Person>> times = new HashMap<>();

        LocalTime beginInTolerance = section.startTime().minusMinutes(TOLERANCE);
        LocalTime endInTolerance = section.startTime().plusMinutes(TOLERANCE);

        LocalTime endLateTolerance = section.startTime().plusMinutes(LATE_TOLERANCE);

        LocalTime beginOutTolerance = section.endTime().minusMinutes(TOLERANCE);
        LocalTime endOutTolerance = section.endTime().plusMinutes(TOLERANCE);

        for (int i = 0; i < days.size(); i++) {
            generateTimes(section, times, types);
            runSimulation(section, days.get(i), times,
                    beginInTolerance, endInTolerance, endLateTolerance,
                    beginOutTolerance, endOutTolerance);
            times = new HashMap<>();
        }
    }

    protected static AttendanceTypeParam doChance(List<AttendanceTypeParam> types) {
        if (types.size() == 0)
            throw new IllegalArgumentException("No percents provided");
        types.sort(new AttendanceTypeParamComparator());

        int sum = types.get(0).percent;
        for (int i = 1; i < types.size(); i++) {
            sum += types.get(i).percent;
        }
        if (sum != 100)
            throw new IllegalArgumentException("Percents must add up to 100");

        Integer rand = random.nextInt(1, 101);
        int lower = 0;
        for (int i = 0; i < types.size() - 1; i++) {
            if (lower < rand && rand <= lower + types.get(i).percent)
                return types.get(i);
            lower += types.get(i).percent;
        }

        return types.get(types.size() - 1);
    }

    /**
     * @deprecated
     * @param percents
     * @return
     */
    // TODO: DELETE
    protected static int doChance(Integer... percents) {
        if (percents.length == 0)
            throw new IllegalArgumentException("No percents provided");
        Arrays.sort(percents);
        int sum = percents[0];
        for (int i = 1; i < percents.length; i++) {
            sum += percents[i];
        }
        if (sum != 100)
            throw new IllegalArgumentException("Percents must add up to 100");

        Integer rand = random.nextInt(1, 101);
        for (int i = 0; i < percents.length - 1; i++) {
            int lower = i == 0 ? 0 : percents[i - 1];
            if (lower < rand && rand <= lower + percents[i])
                return percents[i];
        }

        return percents[percents.length - 1];
    }

    private static void generateTimes(Section section, HashMap<LocalTime, List<Person>> times,
            List<AttendanceTypeParam> types) {

        long minutes;
        LocalTime inTime = null;
        LocalTime outTime = null;
        for (Enrollment enrollment : section.enrollments()) {
            
            if (enrollment.type() != EnrollmentType.STUDENT) continue;

            AttendanceTypeParam params = doChance(types);
            minutes = ChronoUnit.MINUTES.between(params.beginIn, params.endIn);
            inTime = params.beginIn.plusMinutes(random.nextLong(minutes));
            minutes = ChronoUnit.MINUTES.between(params.beginOut, params.endOut);
            outTime = params.beginOut.plusMinutes(random.nextLong(minutes));

            if (!times.containsKey(inTime))
                times.put(inTime, new ArrayList<>());
            times.get(inTime).add(enrollment.person());
            if (!times.containsKey(outTime))
                times.put(outTime, new ArrayList<>());
            times.get(outTime).add(enrollment.person());
        }
    }

    private void runSimulation(Section course, LocalDate day, HashMap<LocalTime, List<Person>> times,
            LocalTime beginInTolerance,
            LocalTime endInTolerance, LocalTime endLateTolerance, LocalTime beginOutTolerance,
            LocalTime endOutTolerance) {

        long rid = 0L;

        LocalTime currentTime = beginInTolerance;
        while (currentTime.isBefore(endOutTolerance) || currentTime.equals(endOutTolerance)) {
            System.out.println("TICK: " + currentTime);
            if (times.containsKey(currentTime)) {
                for (Person student : times.get(currentTime)) {
                    AttendanceRecordType type = null;
                    if (!repo.stream().anyMatch((record) -> record.sid().equals(student.sid())
                            && record.time().toLocalDate().equals(day))) {
                        if (currentTime.isBefore(endInTolerance) || currentTime.equals(endInTolerance)) {
                            type = AttendanceRecordType.ARRIVED;
                        } else if (currentTime.isBefore(endLateTolerance) || currentTime.equals(endLateTolerance)) {
                            type = AttendanceRecordType.ARRIVED_LATE;
                        } else {
                            type = AttendanceRecordType.ARRIVED_INVALID;
                        }
                    } else {
                        if ((currentTime.isAfter(beginOutTolerance) || currentTime.equals(beginOutTolerance)) &&
                                (currentTime.isBefore(endOutTolerance) || currentTime.equals(endOutTolerance))) {
                            type = AttendanceRecordType.LEFT;
                        } else {
                            type = AttendanceRecordType.LEFT_INVALID;
                        }
                    }

                    LocalDateTime dateTime = LocalDateTime.of(day, currentTime);

                    AttendanceRecord record = new AttendanceRecord(rid++, course.location(),
                            dateTime, student.sid(), type);

                    repo.add(record);

                    // System.out.printf("[%s] %s - %s%n", currentTime.toString(), type.toString(), student.toString());
                }
            }
            currentTime = currentTime.plusMinutes(1L);
        }
    }

    @Override
    public Repository<AttendanceRecord> getRepository() {
        return repo;
    }
}
