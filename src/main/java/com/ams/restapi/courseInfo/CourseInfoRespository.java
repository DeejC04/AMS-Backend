package com.ams.restapi.courseInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseInfoRespository extends JpaRepository<CourseInfoLog, Long> {

    List<CourseInfoLog> findByCourseId(Long courseId);

}