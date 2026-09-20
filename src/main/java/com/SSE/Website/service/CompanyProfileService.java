package com.SSE.Website.service;

import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.CompanyProfile;
import com.SSE.Website.repository.CompanyProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;

    public CompanyProfile createCompanyProfile(CompanyProfile companyProfile) {
        return companyProfileRepository.save(companyProfile);
    }

    public CompanyProfile getCompanyProfile() {
        return companyProfileRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public CompanyProfile updateCompanyProfile(Long id, CompanyProfile companyProfile) {

        CompanyProfile existing = companyProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found"));

        existing.setCompanyName(companyProfile.getCompanyName());
        existing.setEstablishedYear(companyProfile.getEstablishedYear());
        existing.setShortDescription(companyProfile.getShortDescription());
        existing.setAbout(companyProfile.getAbout());
        existing.setMission(companyProfile.getMission());
        existing.setVision(companyProfile.getVision());
        existing.setHeadOffice(companyProfile.getHeadOffice());
        existing.setEmail(companyProfile.getEmail());
        existing.setPhone(companyProfile.getPhone());
        existing.setWebsite(companyProfile.getWebsite());
        existing.setLogoUrl(companyProfile.getLogoUrl());

        return companyProfileRepository.save(existing);
    }

    public void deleteCompanyProfile(Long id) {
        companyProfileRepository.deleteById(id);
    }
}