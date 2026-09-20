package com.SSE.Website.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "OTP is required")
    private String otp;
}