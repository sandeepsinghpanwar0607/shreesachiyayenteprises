package com.SSE.Website.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.SSE.Website.dto.CreateSalesRequest;
import com.SSE.Website.dto.RegisterRequest;
import com.SSE.Website.dto.UserResponse;
import com.SSE.Website.entity.Role;
import com.SSE.Website.entity.User;
import com.SSE.Website.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final OtpService otpService;


    // ==========================================
    // CUSTOMER REGISTRATION
    // ==========================================
    public UserResponse registerCustomer(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        if (userRepository
                .findByMobileNumber(request.getMobileNumber())
                .isPresent()) {

            throw new RuntimeException(
                    "Mobile number already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());

        user.setCompanyName(request.getCompanyName());

        user.setEmail(request.getEmail());

        user.setMobileNumber(request.getMobileNumber());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(Role.CUSTOMER);

        // OTP verification ke baad active hoga
        user.setActive(false);

        // Mobile OTP verification pending
        user.setMobileVerified(false);

        User savedUser = userRepository.save(user);

        // Mobile OTP generate
        otpService.generateOtp(savedUser);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getActive()
        );
    }


    // ==========================================
    // CREATE SALES USER
    // ADMIN ONLY
    // ==========================================
    public UserResponse createSalesUser(
            CreateSalesRequest request) {

        // Check email
        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        User salesUser = new User();

        salesUser.setName(request.getName());

        salesUser.setEmail(request.getEmail());

        salesUser.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // SALES role automatically
        salesUser.setRole(Role.SALES);

        // Admin-created Sales account
        salesUser.setActive(true);

        // Sales ko OTP ki requirement nahi
        salesUser.setMobileVerified(false);

        User savedUser =
                userRepository.save(salesUser);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getActive()
        );
    }
}