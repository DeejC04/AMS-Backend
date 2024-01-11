package com.ams.restapi.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOidcUserService customOidcUserService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AdminEmailService adminEmailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(new CustomDeviceAuthenticationFilter(deviceService), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/").permitAll();
                auth.requestMatchers("/favicon.ico").permitAll();
                auth.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
                auth.requestMatchers("/sections").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.requestMatchers(HttpMethod.POST, "/attendance").permitAll();
                auth.requestMatchers(HttpMethod.POST, "/readers").permitAll();
                auth.requestMatchers(HttpMethod.GET, "/readers").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.requestMatchers(HttpMethod.PUT, "/readers").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.requestMatchers("/esp/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR");
                auth.anyRequest().authenticated();
            })
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/readers/**")
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(customOidcUserService)
                )
            )
            .formLogin(withDefaults()) // can be removed to jump straight to Google OAuth
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter(adminEmailService));
        return jwtConverter;
    }
}
