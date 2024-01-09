package com.ams.restapi.authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private Set<String> validTokens = new HashSet<>();

    public DeviceService() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("valid-esptokens.csv"))) {
            while ((line = br.readLine()) != null) {
                validTokens.add(line.trim());
                System.out.println("Loaded valid token: " + line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateDevice(String token) {
        return validTokens.contains(token);
    }
}
