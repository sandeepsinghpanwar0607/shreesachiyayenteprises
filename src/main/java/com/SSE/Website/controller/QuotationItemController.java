package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.QuotationItem;
import com.SSE.Website.service.QuotationItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quotation-items")
@RequiredArgsConstructor
public class QuotationItemController {

    private final QuotationItemService quotationItemService;


    // Create Quotation Item
    @PostMapping
    public ResponseEntity<QuotationItem> createQuotationItem(
            @RequestBody QuotationItem quotationItem) {

        return ResponseEntity.ok(
                quotationItemService.createQuotationItem(
                        quotationItem
                )
        );
    }


    // Get All Quotation Items
    @GetMapping
    public ResponseEntity<List<QuotationItem>> getAllQuotationItems() {

        return ResponseEntity.ok(
                quotationItemService.getAllQuotationItems()
        );
    }


    // Get Quotation Item By ID
    @GetMapping("/{id}")
    public ResponseEntity<QuotationItem> getQuotationItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                quotationItemService.getQuotationItemById(id)
        );
    }


    // Get Items By Quotation ID
    @GetMapping("/quotation/{quotationId}")
    public ResponseEntity<List<QuotationItem>> getItemsByQuotationId(
            @PathVariable Long quotationId) {

        return ResponseEntity.ok(
                quotationItemService.getItemsByQuotationId(
                        quotationId
                )
        );
    }


    // Update Quotation Item
    @PutMapping("/{id}")
    public ResponseEntity<QuotationItem> updateQuotationItem(
            @PathVariable Long id,
            @RequestBody QuotationItem quotationItem) {

        return ResponseEntity.ok(
                quotationItemService.updateQuotationItem(
                        id,
                        quotationItem
                )
        );
    }


    // Delete Quotation Item
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuotationItem(
            @PathVariable Long id) {

        quotationItemService.deleteQuotationItem(id);

        return ResponseEntity.ok(
                "Quotation item deleted successfully"
        );
    }
}