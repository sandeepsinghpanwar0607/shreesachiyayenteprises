package com.SSE.Website.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.SSE.Website.entity.Role;
import com.SSE.Website.entity.User;
import com.SSE.Website.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@sse.com").isEmpty()) {

            User admin = User.builder()
                    .name("SSE Admin")
                    .email("admin@sse.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            userRepository.save(admin);

            System.out.println("ADMIN user created successfully");
        }
    }
}