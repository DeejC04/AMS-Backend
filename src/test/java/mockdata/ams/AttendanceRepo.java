package mockdata.ams;

import java.io.PrintWriter;
import java.util.ArrayList;

public class AttendanceRepo extends ArrayList<AttendanceRecord> implements Repository<AttendanceRecord> {

    @Override
    public void exportToCSV(PrintWriter out) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportToCSV'");
    }
    
}
