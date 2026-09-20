package com.SSE.Website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SSE.Website.entity.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByUserId(Long userId);
}