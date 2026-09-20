package com.SSE.Website.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private Integer establishedYear;

    private String shortDescription;

    private String about;

    private String mission;

    private String vision;

    private String headOffice;

    private String email;

    private String phone;

    private String website;

    private String logoUrl;
}