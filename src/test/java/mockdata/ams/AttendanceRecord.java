package mockdata.ams;
import java.time.LocalDateTime;
import java.time.ZoneId;

import mockdata.peoplesoft.model.Location;

public record AttendanceRecord(Long id, Location room, LocalDateTime time, String sid, AttendanceRecordType type) {
    public enum AttendanceRecordType {
        ARRIVED, ARRIVED_LATE, ARRIVED_INVALID, LEFT, LEFT_INVALID
    }

    public String toCSV() {
        // ZoneId zone = ZoneId.of("MST", ZoneId.SHORT_IDS);
        return String.format("%s, %s, %s, %s, %s",
            // room.room(), time.toEpochSecond(zone.getRules().getOffset(time)), sid, type.name()
            room.room(), time.toLocalDate(), time.toLocalTime(), sid, type.name()
        );
    }
}
