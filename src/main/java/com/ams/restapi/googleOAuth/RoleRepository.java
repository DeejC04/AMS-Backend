package com.ams.restapi.googleOAuth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByUser(User user);
    List<Role> findBySection(Section section);
    List<Role> findByUserAndRole(User user, Role.RoleType role);
}
