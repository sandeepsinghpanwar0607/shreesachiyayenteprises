package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.Product;
import com.SSE.Website.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    // ==========================================
    // CREATE PRODUCT
    // ADMIN ONLY
    // ==========================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product) {

        return ResponseEntity.ok(
                productService.createProduct(product)
        );
    }


    // ==========================================
    // GET ALL PRODUCTS
    // CUSTOMER + SALES + ADMIN
    // ==========================================
    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SALES', 'ADMIN')")
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }


    // ==========================================
    // GET PRODUCT BY ID
    // CUSTOMER + SALES + ADMIN
    // ==========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SALES', 'ADMIN')")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }


    // ==========================================
    // UPDATE PRODUCT
    // ADMIN ONLY
    // ==========================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        return ResponseEntity.ok(
                productService.updateProduct(id, product)
        );
    }


    // ==========================================
    // DELETE PRODUCT
    // ADMIN ONLY
    // ==========================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );
    }
}