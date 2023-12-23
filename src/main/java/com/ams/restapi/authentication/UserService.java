package com.ams.restapi.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public User findOrCreateUser(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> createUser(email, name));
    }

    private User createUser(String email, String name) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        assignDefaultRole(newUser);
        return newUser;
    }

    private void assignDefaultRole(User user) {
        Role defaultRole = roleRepository.findByRole(Role.RoleType.DEFAULT)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        defaultRole = entityManager.merge(defaultRole);
        user.getRoles().add(defaultRole);
    }
}

