package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.Category;
import com.SSE.Website.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    // ==========================================
    // CREATE CATEGORY
    // ADMIN ONLY
    // ==========================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category) {

        return ResponseEntity.ok(
                categoryService.createCategory(category)
        );
    }


    // ==========================================
    // GET ALL CATEGORIES
    // CUSTOMER + SALES + ADMIN
    // ==========================================
    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SALES', 'ADMIN')")
    public ResponseEntity<List<Category>> getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }


    // ==========================================
    // GET CATEGORY BY ID
    // CUSTOMER + SALES + ADMIN
    // ==========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SALES', 'ADMIN')")
    public ResponseEntity<Category> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }


    // ==========================================
    // UPDATE CATEGORY
    // ADMIN ONLY
    // ==========================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category category) {

        return ResponseEntity.ok(
                categoryService.updateCategory(id, category)
        );
    }


    // ==========================================
    // DELETE CATEGORY
    // ADMIN ONLY
    // ==========================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                "Category deleted successfully"
        );
    }
}