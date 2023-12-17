package com.ams.restapi.googleOAuth;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class GoogleOAuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private RoleRepository roleRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @GetMapping("/")
    public String home() {
        return "Hello World";
    } 

    @GetMapping("/secure")
    public String secured(){
        return "index";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/roles")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/users/{userId}/roles")
    public ResponseEntity<Set<Role>> getUserRoles(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<Role> roles = roleRepository.findByUsersContains(user);
        return ResponseEntity.ok(roles);
    }
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @GetMapping("/sections")
    public ResponseEntity<List<Section>> getAllSections() {
        List<Section> sections = sectionRepository.findAll();
        return ResponseEntity.ok(sections);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/admin/sections")
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        Section newSection = sectionRepository.save(section);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSection);
    }

    @PutMapping("/admin/users/{userId}/assign-role-section")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> assignRoleAndSectionToUser(
            @PathVariable Long userId, 
            @RequestParam("roleId") Long roleId,
            @RequestParam("sectionId") Long sectionId,
            Authentication authentication) {

        String userEmail;
        if (authentication.getPrincipal() instanceof UserDetails) {
            userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            userEmail = jwt.getClaimAsString("email");  // Adjust the claim name as per your token structure
        } else {
            throw new IllegalArgumentException("Unknown principal type");
        }

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        Section section = sectionRepository.findById(sectionId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));

        role.getSections().add(section);
        user.getRoles().add(role);
        userRepository.save(user);

        // Fetch the current Authentication and update the security context
        User updatedUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> updatedAuthorities = updatedUser.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.getAuthority()))
            .collect(Collectors.toSet());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return ResponseEntity.ok().build();
    }
}
