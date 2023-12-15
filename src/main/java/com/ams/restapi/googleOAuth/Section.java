package com.ams.restapi.googleOAuth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sectionName;

    public Long getId() {
        return id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setId(Long sectionId) {
        this.id = sectionId;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
