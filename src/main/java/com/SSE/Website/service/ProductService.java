package com.SSE.Website.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.Category;
import com.SSE.Website.entity.Product;
import com.SSE.Website.repository.CategoryRepository;
import com.SSE.Website.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public Product createProduct(Product product) {

        Long categoryId = product.getCategory().getId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        product.setCategory(category);

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));
    }

    public Product updateProduct(Long id, Product product) {

        Product existing = getProductById(id);

        existing.setName(product.getName());

        existing.setModelNumber(product.getModelNumber());

        existing.setDescription(product.getDescription());

        existing.setPrice(product.getPrice());

        existing.setAvailability(product.getAvailability());

        // Update image URL if provided
        if (product.getImageUrl() != null) {
            existing.setImageUrl(product.getImageUrl());
        }

        if (product.getCategory() != null) {

            Long categoryId = product.getCategory().getId();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Category not found"));

            existing.setCategory(category);
        }

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }

    public Product updateProductImage(
            Long productId,
            String imageUrl) {

        Product product = getProductById(productId);

        product.setImageUrl(imageUrl);

        return productRepository.save(product);
    }
}