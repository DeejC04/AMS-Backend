package com.ams.restapi.courseInfo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseInfoRespository extends JpaRepository<CourseInfo, Long> {
    // public Optional<CourseInfo> findByRoomAndTimeBetween
}