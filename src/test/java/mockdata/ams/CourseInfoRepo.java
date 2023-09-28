package mockdata.ams;

import java.io.PrintWriter;
import java.util.ArrayList;

import mockdata.peoplesoft.model.Course;

public class CourseInfoRepo extends ArrayList<Course> implements Repository<Course> {

    @Override
    public void exportToCSV(PrintWriter out) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportToCSV'");
    }
    
}
