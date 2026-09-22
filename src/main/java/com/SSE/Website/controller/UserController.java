package com.SSE.Website.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.dto.AccountSettingsRequest;
import com.SSE.Website.dto.CreateSalesRequest;
import com.SSE.Website.dto.RegisterRequest;
import com.SSE.Website.dto.UserResponse;
import com.SSE.Website.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ==========================================
    // CUSTOMER REGISTRATION
    // PUBLIC
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerCustomer(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                userService.registerCustomer(request)
        );
    }

    // ==========================================
    // CREATE SALES USER
    // ADMIN ONLY
    // ==========================================

    @PostMapping("/create-sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createSalesUser(
            @Valid @RequestBody CreateSalesRequest request) {

        return ResponseEntity.ok(
                userService.createSalesUser(request)
        );
    }

    // ==========================================
    // ACCOUNT SETTINGS
    // LOGGED-IN USER
    // ==========================================

    @PutMapping("/account-settings")
    public ResponseEntity<UserResponse> updateAccountSettings(
            @Valid @RequestBody AccountSettingsRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                userService.updateAccountSettings(email, request)
        );
    }
}