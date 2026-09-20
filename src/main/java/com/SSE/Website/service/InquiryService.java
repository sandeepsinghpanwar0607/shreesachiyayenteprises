package com.SSE.Website.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.Inquiry;
import com.SSE.Website.entity.User;
import com.SSE.Website.repository.InquiryRepository;
import com.SSE.Website.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    // ==========================================
    // GET LOGGED-IN USER
    // ==========================================
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication.getName() == null) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged-in user not found"
                        ));
    }

    // ==========================================
    // CREATE INQUIRY
    // CUSTOMER
    // ==========================================
    public Inquiry createInquiry(Inquiry inquiry) {

        User user = getLoggedInUser();

        inquiry.setUser(user);

        inquiry.setCustomerName(user.getName());
        inquiry.setEmail(user.getEmail());
        inquiry.setCompanyName(user.getCompanyName());
        inquiry.setPhone(user.getMobileNumber());

        inquiry.setStatus("NEW");
        inquiry.setCreatedAt(LocalDateTime.now());

        return inquiryRepository.save(inquiry);
    }

    // ==========================================
    // GET ALL INQUIRIES
    // SALES + ADMIN
    // ==========================================
    public List<Inquiry> getAllInquiries() {

        return inquiryRepository.findAll();
    }

    // ==========================================
    // GET MY INQUIRIES
    // CUSTOMER
    // ==========================================
    public List<Inquiry> getMyInquiries() {

        User user = getLoggedInUser();

        return inquiryRepository.findByUserId(
                user.getId()
        );
    }

    // ==========================================
    // GET INQUIRY BY ID
    // ==========================================
    public Inquiry getInquiryById(Long id) {

        return inquiryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inquiry not found"
                        ));
    }

    // ==========================================
    // UPDATE INQUIRY
    // SALES + ADMIN
    // ==========================================
    public Inquiry updateInquiry(
            Long id,
            Inquiry inquiry) {

        Inquiry existing = getInquiryById(id);

        existing.setCustomerName(
                inquiry.getCustomerName()
        );

        existing.setEmail(
                inquiry.getEmail()
        );

        existing.setPhone(
                inquiry.getPhone()
        );

        existing.setCompanyName(
                inquiry.getCompanyName()
        );

        existing.setMessage(
                inquiry.getMessage()
        );

        if (inquiry.getStatus() != null) {

            existing.setStatus(
                    inquiry.getStatus()
            );
        }

        return inquiryRepository.save(existing);
    }

    // ==========================================
    // DELETE INQUIRY
    // ADMIN ONLY
    // ==========================================
    public void deleteInquiry(Long id) {

        Inquiry existing = getInquiryById(id);

        inquiryRepository.delete(existing);
    }
}