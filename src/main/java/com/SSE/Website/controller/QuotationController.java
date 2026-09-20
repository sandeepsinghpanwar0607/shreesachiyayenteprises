package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SSE.Website.dto.QuotationStatusRequest;
import com.SSE.Website.entity.Quotation;
import com.SSE.Website.service.QuotationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;


    // =====================================================
    // CREATE QUOTATION
    // SALES + ADMIN
    // =====================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Quotation> createQuotation(
            @RequestBody Quotation quotation) {

        return ResponseEntity.ok(
                quotationService.createQuotation(quotation)
        );
    }


    // =====================================================
    // GET ALL QUOTATIONS
    // SALES + ADMIN
    // =====================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<List<Quotation>> getAllQuotations() {

        return ResponseEntity.ok(
                quotationService.getAllQuotations()
        );
    }


    // =====================================================
    // GET MY QUOTATIONS
    // CUSTOMER
    // =====================================================

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Quotation>> getMyQuotations() {

        return ResponseEntity.ok(
                quotationService.getMyQuotations()
        );
    }


    // =====================================================
    // GET MY QUOTATION BY ID
    // CUSTOMER
    // =====================================================

    @GetMapping("/my/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Quotation> getMyQuotationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                quotationService.getMyQuotationById(id)
        );
    }


    // =====================================================
    // GET QUOTATION BY ID
    // SALES + ADMIN
    // =====================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Quotation> getQuotationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                quotationService.getQuotationById(id)
        );
    }


    // =====================================================
    // UPDATE QUOTATION
    // SALES + ADMIN
    // =====================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Quotation> updateQuotation(
            @PathVariable Long id,
            @RequestBody Quotation quotation) {

        return ResponseEntity.ok(
                quotationService.updateQuotation(
                        id,
                        quotation
                )
        );
    }


    // =====================================================
    // SEND QUOTATION
    // SALES + ADMIN
    //
    // DRAFT -> SENT
    // =====================================================

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Quotation> updateQuotationStatus(
            @PathVariable Long id,
            @RequestBody QuotationStatusRequest request) {

        return ResponseEntity.ok(
                quotationService.updateQuotationStatus(
                        id,
                        request.getStatus()
                )
        );
    }


    // =====================================================
    // CUSTOMER ACCEPT / REJECT QUOTATION
    //
    // SENT -> ACCEPTED
    // SENT -> REJECTED
    // =====================================================

    @PutMapping("/{id}/respond")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Quotation> customerRespondToQuotation(
            @PathVariable Long id,
            @RequestBody QuotationStatusRequest request) {

        return ResponseEntity.ok(
                quotationService.customerRespondToQuotation(
                        id,
                        request.getStatus()
                )
        );
    }


    // =====================================================
    // DELETE QUOTATION
    // ADMIN ONLY
    // =====================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteQuotation(
            @PathVariable Long id) {

        quotationService.deleteQuotation(id);

        return ResponseEntity.ok(
                "Quotation deleted successfully"
        );
    }
}