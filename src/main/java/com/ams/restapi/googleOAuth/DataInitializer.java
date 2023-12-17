package com.ams.restapi.googleOAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initAdminUser() {
        String adminEmail = "";
        User adminUser = userRepository.findByEmail(adminEmail).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(adminEmail);
            return userRepository.save(newUser);
        });

        if (roleRepository.findByUsersContains(adminUser).isEmpty()) {
            ensureRolesExist();
            Role defaultRole = roleRepository.findByRole(Role.RoleType.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            adminUser.getRoles().add(defaultRole);
            userRepository.save(adminUser);
        }
    }

    private void ensureRolesExist() {
        for (Role.RoleType roleType : Role.RoleType.values()) {
            roleRepository.findByRole(roleType)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(roleType);
                    return roleRepository.save(newRole);
                });
        }
    }
}
