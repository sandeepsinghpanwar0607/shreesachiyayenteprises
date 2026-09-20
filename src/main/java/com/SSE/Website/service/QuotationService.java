package com.SSE.Website.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.Inquiry;
import com.SSE.Website.entity.Quotation;
import com.SSE.Website.entity.User;
import com.SSE.Website.repository.InquiryRepository;
import com.SSE.Website.repository.QuotationRepository;
import com.SSE.Website.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    // GET LOGGED-IN USER
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

    // CREATE QUOTATION
    public Quotation createQuotation(Quotation quotation) {

        if (quotation.getInquiry() == null
                || quotation.getInquiry().getId() == null) {

            throw new RuntimeException(
                    "Inquiry is required"
            );
        }

        Long inquiryId =
                quotation.getInquiry().getId();

        Inquiry inquiry =
                inquiryRepository.findById(inquiryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inquiry not found"
                        ));

        quotation.setInquiry(inquiry);
        quotation.setCreatedAt(LocalDateTime.now());
        quotation.setStatus("DRAFT");

        return quotationRepository.save(quotation);
    }

    // GET ALL QUOTATIONS
    public List<Quotation> getAllQuotations() {

        return quotationRepository.findAll();
    }

    // GET MY QUOTATIONS
    public List<Quotation> getMyQuotations() {

        User user = getLoggedInUser();

        return quotationRepository
                .findByInquiryUserId(user.getId());
    }

    // GET QUOTATION BY ID
    public Quotation getQuotationById(Long id) {

        return quotationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation not found"
                        ));
    }

    // GET MY QUOTATION BY ID
    public Quotation getMyQuotationById(Long id) {

        User user = getLoggedInUser();

        Quotation quotation =
                quotationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation not found"
                        ));

        if (quotation.getInquiry() == null
                || quotation.getInquiry().getUser() == null
                || !quotation.getInquiry()
                        .getUser()
                        .getId()
                        .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to access this quotation"
            );
        }

        return quotation;
    }

    // UPDATE QUOTATION
    public Quotation updateQuotation(
            Long id,
            Quotation quotation) {

        Quotation existing =
                getQuotationById(id);

        existing.setQuotationNumber(
                quotation.getQuotationNumber()
        );

        existing.setTotalAmount(
                quotation.getTotalAmount()
        );

        existing.setValidUntil(
                quotation.getValidUntil()
        );

        existing.setNotes(
                quotation.getNotes()
        );

        if (quotation.getInquiry() != null
                && quotation.getInquiry().getId() != null) {

            Long inquiryId =
                    quotation.getInquiry().getId();

            Inquiry inquiry =
                    inquiryRepository.findById(inquiryId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Inquiry not found"
                            ));

            existing.setInquiry(inquiry);
        }

        return quotationRepository.save(existing);
    }

    // SALES / ADMIN
    // DRAFT -> SENT
    public Quotation updateQuotationStatus(
            Long id,
            String newStatus) {

        Quotation quotation =
                getQuotationById(id);

        String currentStatus =
                quotation.getStatus();

        if (newStatus == null
                || newStatus.isBlank()) {

            throw new RuntimeException(
                    "Status is required"
            );
        }

        newStatus = newStatus.toUpperCase();

        if (currentStatus.equals("DRAFT")
                && newStatus.equals("SENT")) {

            quotation.setStatus("SENT");

        } else {

            throw new RuntimeException(
                    "Sales/Admin can only change quotation status from DRAFT to SENT"
            );
        }

        return quotationRepository.save(quotation);
    }

    // CUSTOMER
    // SENT -> ACCEPTED / REJECTED
    public Quotation customerRespondToQuotation(
            Long id,
            String newStatus) {

        User user = getLoggedInUser();

        Quotation quotation =
                quotationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation not found"
                        ));

        // OWNERSHIP CHECK
        if (quotation.getInquiry() == null
                || quotation.getInquiry().getUser() == null
                || !quotation.getInquiry()
                        .getUser()
                        .getId()
                        .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to respond to this quotation"
            );
        }

        if (newStatus == null
                || newStatus.isBlank()) {

            throw new RuntimeException(
                    "Status is required"
            );
        }

        newStatus = newStatus.toUpperCase();

        // Only SENT quotation can be accepted/rejected
        if (!"SENT".equalsIgnoreCase(
                quotation.getStatus())) {

            throw new RuntimeException(
                    "Only SENT quotations can be accepted or rejected"
            );
        }

        if (newStatus.equals("ACCEPTED")) {

            quotation.setStatus("ACCEPTED");

        } else if (newStatus.equals("REJECTED")) {

            quotation.setStatus("REJECTED");

        } else {

            throw new RuntimeException(
                    "Customer can only ACCEPT or REJECT quotation"
            );
        }

        return quotationRepository.save(quotation);
    }

    // DELETE QUOTATION
    public void deleteQuotation(Long id) {

        Quotation existing =
                getQuotationById(id);

        quotationRepository.delete(existing);
    }
}