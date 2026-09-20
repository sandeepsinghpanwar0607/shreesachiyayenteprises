package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.Inquiry;
import com.SSE.Website.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // ==========================================
    // CREATE INQUIRY
    // CUSTOMER + SALES + ADMIN
    // ==========================================
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SALES', 'ADMIN')")
    public ResponseEntity<Inquiry> createInquiry(
            @RequestBody Inquiry inquiry) {

        return ResponseEntity.ok(
                inquiryService.createInquiry(inquiry)
        );
    }

    // ==========================================
    // GET MY INQUIRIES
    // CUSTOMER ONLY
    // ==========================================
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Inquiry>> getMyInquiries() {

        return ResponseEntity.ok(
                inquiryService.getMyInquiries()
        );
    }

    // ==========================================
    // GET ALL INQUIRIES
    // SALES + ADMIN
    // ==========================================
    @GetMapping
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<List<Inquiry>> getAllInquiries() {

        return ResponseEntity.ok(
                inquiryService.getAllInquiries()
        );
    }

    // ==========================================
    // GET INQUIRY BY ID
    // SALES + ADMIN
    // ==========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Inquiry> getInquiryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inquiryService.getInquiryById(id)
        );
    }

    // ==========================================
    // UPDATE INQUIRY
    // SALES + ADMIN
    // ==========================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Inquiry> updateInquiry(
            @PathVariable Long id,
            @RequestBody Inquiry inquiry) {

        return ResponseEntity.ok(
                inquiryService.updateInquiry(id, inquiry)
        );
    }

    // ==========================================
    // DELETE INQUIRY
    // ADMIN ONLY
    // ==========================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteInquiry(
            @PathVariable Long id) {

        inquiryService.deleteInquiry(id);

        return ResponseEntity.ok(
                "Inquiry deleted successfully"
        );
    }
}