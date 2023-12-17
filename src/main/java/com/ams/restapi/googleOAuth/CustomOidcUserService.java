package com.ams.restapi.googleOAuth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomOidcUserService extends OidcUserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        logger.info("Attempting to find or create user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(oidcUser, email));

        if (user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByRole(Role.RoleType.DEFAULT)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            defaultRole = entityManager.merge(defaultRole);
            user.getRoles().add(defaultRole);
        }

        userRepository.save(user);

        Set<GrantedAuthority> combinedAuthorities = new HashSet<>(oidcUser.getAuthorities());
        user.getRoles().forEach(role -> combinedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority())));

        return new DefaultOidcUser(combinedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

    private User createUser(OidcUser oidcUser, String email) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(oidcUser.getAttribute("name"));
        logger.info("Creating new user with email: {}", email);
        return newUser;
    }
}



