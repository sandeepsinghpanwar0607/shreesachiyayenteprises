package com.SSE.Website.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Invalid mobile number"
    )
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    @Size(
        min = 6,
        message = "Password must be at least 6 characters"
    )
    private String password;
}