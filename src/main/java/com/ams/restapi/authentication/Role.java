package com.ams.restapi.authentication;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Role implements GrantedAuthority {
    private @Id @GeneratedValue Long id;

    public static enum RoleType {
        INSTRUCTOR, TA, ADMIN, DEFAULT, ESP
    }

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "role_sections",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<Section> sections = new HashSet<>();
    
    public Role() {}

    @Override
    public String getAuthority() {
        return "ROLE_" + this.role.name();
    }

    public Long getId() {
        return id;
    }

    public RoleType getRole() {
        return role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setId(Long roleId) {
        this.id = roleId;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }
}
