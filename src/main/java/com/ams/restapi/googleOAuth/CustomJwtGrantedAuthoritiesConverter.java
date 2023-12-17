package com.ams.restapi.googleOAuth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Check if the email matches a specific address and assign "ADMIN" role
        if ("".equals(email)) {
            Role adminRole = new Role();
            adminRole.setRole(Role.RoleType.ADMIN);
            authorities.add(new SimpleGrantedAuthority(adminRole.getAuthority()));
        }

        return authorities;
    }
}


