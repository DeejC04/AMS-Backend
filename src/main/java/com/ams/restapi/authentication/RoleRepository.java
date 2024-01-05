package com.ams.restapi.authentication;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.RoleType roleType);
    Set<Role> findByUsersContains(User user);
    Set<Role> findBySectionsContains(Section section);
    Set<Role> findByUsersAndRoleContains(User users, Role.RoleType role);
}
