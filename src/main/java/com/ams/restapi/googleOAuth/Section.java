package com.ams.restapi.googleOAuth;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseCode;

    private String sectionId;

    @ManyToMany(mappedBy = "sections")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSectionId() {
        return sectionId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setId(Long sectionId) {
        this.id = sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
