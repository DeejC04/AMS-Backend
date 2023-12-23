package com.ams.restapi.authentication;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CanvasSectionRefresher canvasSectionRefresher;

    @Autowired
    private AdminEmailService adminEmailService;

    @PostConstruct
    public void initAdminUser() {
        adminEmailService.getAdminEmails().forEach(adminEmail -> {
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
            
            try{
                canvasSectionRefresher.updateUserSections();
            } catch (IOException e) {

            }
        });
        
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
