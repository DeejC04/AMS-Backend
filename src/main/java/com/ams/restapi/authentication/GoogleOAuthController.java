package com.ams.restapi.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Controller
public class GoogleOAuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private RoleRepository roleRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @GetMapping("/csrf")
    public @ResponseBody String getCsrfToken(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return csrf.getToken();
    }

    @GetMapping("/websocket")
    public String webSocket(){
        return "webSocket.html";
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
            @RequestParam("sectionId") Long sectionId) {

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        Section section = sectionRepository.findById(sectionId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Section not found"));

        role.getSections().add(section);
        user.getRoles().add(role);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
