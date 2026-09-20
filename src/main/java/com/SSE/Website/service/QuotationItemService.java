package com.SSE.Website.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.Product;
import com.SSE.Website.entity.Quotation;
import com.SSE.Website.entity.QuotationItem;
import com.SSE.Website.repository.ProductRepository;
import com.SSE.Website.repository.QuotationItemRepository;
import com.SSE.Website.repository.QuotationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuotationItemService {

    private final QuotationItemRepository quotationItemRepository;
    private final QuotationRepository quotationRepository;
    private final ProductRepository productRepository;

    // Create Quotation Item
    public QuotationItem createQuotationItem(
            QuotationItem quotationItem) {

        Long quotationId =
                quotationItem.getQuotation().getId();

        Long productId =
                quotationItem.getProduct().getId();

        Quotation quotation =
                quotationRepository.findById(quotationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation not found"
                        ));

        Product product =
                productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        ));

        quotationItem.setQuotation(quotation);
        quotationItem.setProduct(product);

        // Calculate subtotal
        if (quotationItem.getQuantity() != null
                && quotationItem.getUnitPrice() != null) {

            quotationItem.setSubtotal(
                    quotationItem.getQuantity()
                    * quotationItem.getUnitPrice()
            );
        }

        QuotationItem savedItem =
                quotationItemRepository.save(quotationItem);

        // Recalculate quotation total
        updateQuotationTotal(quotationId);

        return savedItem;
    }

    // Calculate quotation total
    private void updateQuotationTotal(Long quotationId) {

        List<QuotationItem> items =
                quotationItemRepository
                        .findByQuotationId(quotationId);

        double total = 0.0;

        for (QuotationItem item : items) {

            if (item.getSubtotal() != null) {
                total += item.getSubtotal();
            }
        }

        Quotation quotation =
                quotationRepository.findById(quotationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation not found"
                        ));

        quotation.setTotalAmount(total);

        quotationRepository.save(quotation);
    }

    // Get All Quotation Items
    public List<QuotationItem> getAllQuotationItems() {
        return quotationItemRepository.findAll();
    }

    // Get Quotation Item By ID
    public QuotationItem getQuotationItemById(Long id) {

        return quotationItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation item not found"
                        ));
    }

    // Get Items By Quotation ID
    public List<QuotationItem> getItemsByQuotationId(
            Long quotationId) {

        return quotationItemRepository
                .findByQuotationId(quotationId);
    }

    // Update Quotation Item
    public QuotationItem updateQuotationItem(
            Long id,
            QuotationItem quotationItem) {

        QuotationItem existing =
                getQuotationItemById(id);

        Long oldQuotationId =
                existing.getQuotation().getId();

        if (quotationItem.getQuantity() != null) {

            existing.setQuantity(
                    quotationItem.getQuantity()
            );
        }

        if (quotationItem.getUnitPrice() != null) {

            existing.setUnitPrice(
                    quotationItem.getUnitPrice()
            );
        }

        if (quotationItem.getQuotation() != null) {

            Long quotationId =
                    quotationItem.getQuotation().getId();

            Quotation quotation =
                    quotationRepository.findById(quotationId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Quotation not found"
                            ));

            existing.setQuotation(quotation);
        }

        if (quotationItem.getProduct() != null) {

            Long productId =
                    quotationItem.getProduct().getId();

            Product product =
                    productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found"
                            ));

            existing.setProduct(product);
        }

        // Recalculate subtotal
        if (existing.getQuantity() != null
                && existing.getUnitPrice() != null) {

            existing.setSubtotal(
                    existing.getQuantity()
                    * existing.getUnitPrice()
            );
        }

        QuotationItem updatedItem =
                quotationItemRepository.save(existing);

        // Recalculate current quotation total
        Long currentQuotationId =
                existing.getQuotation().getId();

        updateQuotationTotal(currentQuotationId);

        // If item was moved to another quotation,
        // recalculate old quotation too
        if (!oldQuotationId.equals(currentQuotationId)) {
            updateQuotationTotal(oldQuotationId);
        }

        return updatedItem;
    }

    // Delete Quotation Item
    public void deleteQuotationItem(Long id) {

        QuotationItem existing =
                getQuotationItemById(id);

        Long quotationId =
                existing.getQuotation().getId();

        quotationItemRepository.delete(existing);

        // Recalculate quotation total
        updateQuotationTotal(quotationId);
    }
}