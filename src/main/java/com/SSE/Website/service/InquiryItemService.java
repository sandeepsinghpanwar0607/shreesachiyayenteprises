package com.SSE.Website.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.*;
import com.SSE.Website.entity.Inquiry;
import com.SSE.Website.entity.InquiryItem;
import com.SSE.Website.entity.Product;
import com.SSE.Website.repository.InquiryItemRepository;
import com.SSE.Website.repository.InquiryRepository;
import com.SSE.Website.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryItemService {

    private final InquiryItemRepository inquiryItemRepository;

    private final InquiryRepository inquiryRepository;

    private final ProductRepository productRepository;


    // Create Inquiry Item
    public InquiryItem createInquiryItem(InquiryItem inquiryItem) {

        Long inquiryId = inquiryItem.getInquiry().getId();

        Long productId = inquiryItem.getProduct().getId();

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inquiry not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        inquiryItem.setInquiry(inquiry);

        inquiryItem.setProduct(product);

        return inquiryItemRepository.save(inquiryItem);
    }


    // Get All Inquiry Items
    public List<InquiryItem> getAllInquiryItems() {

        return inquiryItemRepository.findAll();
    }


    // Get Inquiry Item By ID
    public InquiryItem getInquiryItemById(Long id) {

        return inquiryItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inquiry item not found"));
    }


    // Get All Items By Inquiry ID
    public List<InquiryItem> getItemsByInquiryId(Long inquiryId) {

        return inquiryItemRepository.findByInquiryId(inquiryId);
    }


    // Update Inquiry Item
    public InquiryItem updateInquiryItem(
            Long id,
            InquiryItem inquiryItem) {

        InquiryItem existing = getInquiryItemById(id);

        existing.setQuantity(inquiryItem.getQuantity());


        if (inquiryItem.getInquiry() != null) {

            Long inquiryId = inquiryItem.getInquiry().getId();

            Inquiry inquiry = inquiryRepository.findById(inquiryId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Inquiry not found"));

            existing.setInquiry(inquiry);
        }


        if (inquiryItem.getProduct() != null) {

            Long productId = inquiryItem.getProduct().getId();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            existing.setProduct(product);
        }

        return inquiryItemRepository.save(existing);
    }


    // Delete Inquiry Item
    public void deleteInquiryItem(Long id) {

        InquiryItem existing = getInquiryItemById(id);

        inquiryItemRepository.delete(existing);
    }
}