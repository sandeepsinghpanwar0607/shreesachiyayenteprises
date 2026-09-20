package com.SSE.Website.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.SSE.Website.dto.LoginRequest;
import com.SSE.Website.dto.LoginResponse;
import com.SSE.Website.entity.User;
import com.SSE.Website.repository.UserRepository;
import com.SSE.Website.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        ));


        // Check account status
        if (!Boolean.TRUE.equals(user.getActive())) {

            throw new RuntimeException(
                    "User account is inactive"
            );
        }


        // Verify password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }


        // Generate JWT
        String token =
                jwtService.generateToken(user);


        // Return login response
        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}