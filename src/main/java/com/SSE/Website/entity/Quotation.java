package com.SSE.Website.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quotationNumber;

    private Double totalAmount;

    private String status;

    private LocalDateTime validUntil;

    private LocalDateTime createdAt;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;
}