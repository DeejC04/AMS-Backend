package com.ams.restapi.googleOAuth;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Role {
    private @Id @GeneratedValue Long id;

    public static enum RoleType {
        INSTRUCTOR, TA, UGTA, ADMIN
    }

    @ManyToOne
    @JoinColumn(name = "email")
    private User user;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @Enumerated(EnumType.STRING)
    private RoleType role;
    
    public Role() {}

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Section getSection() {
        return section;
    }

    public RoleType getRole() {
        return role;
    }

    public void setId(Long roleId) {
        this.id = roleId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
