package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.OrderItem;
import com.SSE.Website.service.OrderItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    // ==========================================
    // CREATE ORDER ITEM
    // SALES + ADMIN
    // ==========================================
    @PostMapping
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<OrderItem> createOrderItem(
            @RequestBody OrderItem orderItem) {

        return ResponseEntity.ok(
                orderItemService.createOrderItem(orderItem)
        );
    }

    // ==========================================
    // GET ALL ORDER ITEMS
    // SALES + ADMIN
    // ==========================================
    @GetMapping
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {

        return ResponseEntity.ok(
                orderItemService.getAllOrderItems()
        );
    }

    // ==========================================
    // GET ORDER ITEM BY ID
    // SALES + ADMIN
    // ==========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderItemService.getOrderItemById(id)
        );
    }

    // ==========================================
    // GET ITEMS BY ORDER ID
    // SALES + ADMIN
    // ==========================================
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<List<OrderItem>> getItemsByOrderId(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderItemService.getItemsByOrderId(orderId)
        );
    }

    // ==========================================
    // UPDATE ORDER ITEM
    // SALES + ADMIN
    // ==========================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<OrderItem> updateOrderItem(
            @PathVariable Long id,
            @RequestBody OrderItem orderItem) {

        return ResponseEntity.ok(
                orderItemService.updateOrderItem(
                        id,
                        orderItem
                )
        );
    }

    // ==========================================
    // DELETE ORDER ITEM
    // ADMIN ONLY
    // ==========================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrderItem(
            @PathVariable Long id) {

        orderItemService.deleteOrderItem(id);

        return ResponseEntity.ok(
                "Order item deleted successfully"
        );
    }
}