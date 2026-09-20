package com.SSE.Website.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.Category;
import com.SSE.Website.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public Category updateCategory(Long id, Category category) {

        Category existing = getCategoryById(id);

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());

        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}