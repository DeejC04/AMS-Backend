package com.ams.restapi.attendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.BufferedReader;

@Configuration
class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private static final boolean BEAN = true;

    @Bean
    CommandLineRunner initDatabase(AttendanceRepository repository) {
        if (BEAN) {
            return args -> {
                log.info("BEAN MODE ACTIVATED");
                BufferedReader reader = new BufferedReader(new FileReader("./mock.csv"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split("\\s*,\\s*");
                    log.info("Preloading " + repository.save(
                        new AttendanceLog(tokens[0], Long.parseLong(tokens[1]), tokens[2], tokens[3])));
                }
                reader.close();
            };
        }
        return args -> {log.info("BEAN MODE DEACTIVATED");};
    }

}
