package com.ams.restapi.authentication;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Set<Section> findBySectionId(String sectionId);
}
