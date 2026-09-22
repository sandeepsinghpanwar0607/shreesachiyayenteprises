package com.SSE.Website.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountSettingsRequest {

    @NotBlank
    private String name;

    private String companyName;

    private String mobileNumber;

    private String currentPassword;

    private String newPassword;
}