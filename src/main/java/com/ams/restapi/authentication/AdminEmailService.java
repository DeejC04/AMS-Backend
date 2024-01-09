package com.ams.restapi.authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class AdminEmailService {
    private Set<String> adminEmails = new HashSet<>();

    public AdminEmailService() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("admin_emails.csv"))) {
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
