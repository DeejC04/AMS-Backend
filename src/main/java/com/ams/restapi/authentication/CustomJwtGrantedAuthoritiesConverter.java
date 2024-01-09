package com.ams.restapi.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    
    private final AdminEmailService adminEmailService;

    public CustomJwtGrantedAuthoritiesConverter(AdminEmailService adminEmailService) {
        this.adminEmailService = adminEmailService;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        if (adminEmailService.isAdminEmail(email)) {
            Role adminRole = new Role();
            adminRole.setRole(Role.RoleType.ADMIN);
            authorities.add(new SimpleGrantedAuthority(adminRole.getAuthority()));
        }

        return authorities;
    }
}


