package com.vitalscan.vitalscans.config;

import com.vitalscan.vitalscans.entity.User;
import com.vitalscan.vitalscans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if nurse already exists
        if (!userRepository.existsByUsername("nurse")) {
            User nurse = User.builder()
                    .username("nurse")
                    .password(passwordEncoder.encode("nurse123"))
                    .email("nurse@vitalscan.com")
                    .rollNumber("NURSE001")
                    .mobileNumber("9999999999")
                    .role(User.Role.NURSE)
                    .height(170.0)
                    .weight(70.0)
                    .firstName("Admin")
                    .lastName("Nurse")
                    .isActive(true)
                    .build();

            userRepository.save(nurse);
            System.out.println("Default nurse account created: username=nurse, password=nurse123");
        }
    }
}
