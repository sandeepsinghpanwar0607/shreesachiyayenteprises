package com.SSE.Website.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class QuotationStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}