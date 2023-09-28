package mockdata.ams;

import java.io.PrintWriter;

public interface Repository<E> extends Iterable<E> {
    void exportToCSV(PrintWriter out);
}
