package com.ams.restapi.googleOAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return processOAuth2User(user);
    }

    private OAuth2User processOAuth2User(OAuth2User oauthUser) {
        // Extract email or other identifier
        String email = oauthUser.getAttribute("email");

        // Check if user already exists in the database
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // If not, save the new user
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setGoogleUserId(oauthUser.getAttribute("sub"));
                    newUser.setName(oauthUser.getAttribute("name"));
                    return userRepository.save(newUser);
                });
        return oauthUser;
    }
}

