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
        String adminEmail = "asing365@asu.edu";
        User adminUser = userRepository.findByEmail(adminEmail).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(adminEmail);
            newUser.setName("Admin User");
            return userRepository.save(newUser);
        });

        Role adminRole = roleRepository.findByUserAndRole(adminUser, Role.RoleType.ADMIN).stream().findFirst().orElseGet(() -> {
            Role newRole = new Role();
            newRole.setUser(adminUser);
            newRole.setRole(Role.RoleType.ADMIN);
            return roleRepository.save(newRole);
        });
    }
}
