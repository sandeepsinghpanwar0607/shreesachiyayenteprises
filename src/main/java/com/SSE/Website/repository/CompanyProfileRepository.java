package com.SSE.Website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SSE.Website.entity.CompanyProfile;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

}