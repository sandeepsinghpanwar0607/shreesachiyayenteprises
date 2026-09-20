package com.SSE.Website.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.SSE.Website.entity.Product;
import com.SSE.Website.service.ProductImageService;
import com.SSE.Website.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;
    private final ProductService productService;

    @PostMapping("/{productId}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        // Check product exists
        productService.getProductById(productId);

        // Upload image
        String imageUrl =
                productImageService.uploadImage(file);

        // Save image URL in Product table
        Product product =
                productService.updateProductImage(
                        productId,
                        imageUrl
                );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Product image uploaded successfully",

                        "productId",
                        product.getId().toString(),

                        "imageUrl",
                        product.getImageUrl()
                )
        );
    }
}