package com.SSE.Website.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.dto.LoginRequest;
import com.SSE.Website.dto.LoginResponse;
import com.SSE.Website.dto.VerifyOtpRequest;
import com.SSE.Website.service.AuthService;
import com.SSE.Website.service.OtpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final OtpService otpService;


    // LOGIN API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }


    // VERIFY OTP API
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        otpService.verifyOtp(
                request.getUserId(),
                request.getOtp()
        );

        return ResponseEntity.ok(
                "Mobile number verified successfully. Account activated."
        );
    }
}