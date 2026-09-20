package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.InquiryItem;
import com.SSE.Website.service.InquiryItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inquiry-items")
@RequiredArgsConstructor
public class InquiryItemController {

    private final InquiryItemService inquiryItemService;


    // Create Inquiry Item
    @PostMapping
    public ResponseEntity<InquiryItem> createInquiryItem(
            @RequestBody InquiryItem inquiryItem) {

        return ResponseEntity.ok(
                inquiryItemService.createInquiryItem(inquiryItem)
        );
    }


    // Get All Inquiry Items
    @GetMapping
    public ResponseEntity<List<InquiryItem>> getAllInquiryItems() {

        return ResponseEntity.ok(
                inquiryItemService.getAllInquiryItems()
        );
    }


    // Get Inquiry Item By ID
    @GetMapping("/{id}")
    public ResponseEntity<InquiryItem> getInquiryItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                inquiryItemService.getInquiryItemById(id)
        );
    }


    // Get Items By Inquiry ID
    @GetMapping("/inquiry/{inquiryId}")
    public ResponseEntity<List<InquiryItem>> getItemsByInquiryId(
            @PathVariable Long inquiryId) {

        return ResponseEntity.ok(
                inquiryItemService.getItemsByInquiryId(inquiryId)
        );
    }


    // Update Inquiry Item
    @PutMapping("/{id}")
    public ResponseEntity<InquiryItem> updateInquiryItem(
            @PathVariable Long id,
            @RequestBody InquiryItem inquiryItem) {

        return ResponseEntity.ok(
                inquiryItemService.updateInquiryItem(id, inquiryItem)
        );
    }


    // Delete Inquiry Item
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInquiryItem(
            @PathVariable Long id) {

        inquiryItemService.deleteInquiryItem(id);

        return ResponseEntity.ok(
                "Inquiry item deleted successfully"
        );
    }
}