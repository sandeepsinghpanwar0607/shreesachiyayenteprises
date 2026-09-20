package com.SSE.Website.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SSE.Website.entity.OtpVerification;

@Repository
public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByUserIdOrderByIdDesc(Long userId);
}