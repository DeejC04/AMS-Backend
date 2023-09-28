package mockdata.peoplesoft.model;

import java.util.List;

import mockdata.peoplesoft.model.GeneralStudies.GeneralStudy;
import mockdata.peoplesoft.model.Subjects.Subject;

public record Course(
    String title,
    Subject subject,
    String number,
    Integer units,
    List<GeneralStudy> gs
    // TODO: prereqs
) {}
