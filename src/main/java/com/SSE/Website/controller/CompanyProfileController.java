package com.SSE.Website.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.CompanyProfile;
import com.SSE.Website.service.CompanyProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;

    @PostMapping
    public ResponseEntity<CompanyProfile> createCompanyProfile(
            @RequestBody CompanyProfile companyProfile) {

        return ResponseEntity.ok(
                companyProfileService.createCompanyProfile(companyProfile)
        );
    }

    @GetMapping
    public ResponseEntity<CompanyProfile> getCompanyProfile() {

        return ResponseEntity.ok(
                companyProfileService.getCompanyProfile()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyProfile> updateCompanyProfile(
            @PathVariable Long id,
            @RequestBody CompanyProfile companyProfile) {

        return ResponseEntity.ok(
                companyProfileService.updateCompanyProfile(id, companyProfile)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompanyProfile(
            @PathVariable Long id) {

        companyProfileService.deleteCompanyProfile(id);

        return ResponseEntity.ok("Company profile deleted successfully");
    }
}