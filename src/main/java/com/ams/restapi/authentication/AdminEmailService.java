package com.ams.restapi.authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AdminEmailService {
    private static final String ADMIN_EMAILS_FILE = "admin_emails.csv";
    private Set<String> adminEmails = new HashSet<>();

    @PostConstruct
    public void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(ADMIN_EMAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                adminEmails.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAdminEmail(String email) {
        return adminEmails.contains(email);
    }

    public Set<String> getAdminEmails() {
        return adminEmails;
    }
}
