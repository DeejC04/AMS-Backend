package mockdata.ams;

import java.io.PrintWriter;
import java.util.ArrayList;

import mockdata.peoplesoft.model.Section;

public class SectionInfoRepo extends ArrayList<Section> implements Repository<Section> {

    @Override
    public void exportToCSV(PrintWriter out) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportToCSV'");
    }
    
}
