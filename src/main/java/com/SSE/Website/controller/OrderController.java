package com.SSE.Website.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.SSE.Website.entity.Order;
import com.SSE.Website.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    // ==========================================
    // CREATE ORDER
    // SALES + ADMIN
    // ==========================================

    @PostMapping
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Order> createOrder(
            @RequestBody Order order) {

        return ResponseEntity.ok(
                orderService.createOrder(order)
        );
    }


    // ==========================================
    // GET ALL ORDERS
    // SALES + ADMIN
    // ==========================================

    @GetMapping
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }


    // ==========================================
    // CUSTOMER - MY ORDERS
    // ==========================================

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Order>> getMyOrders() {

        return ResponseEntity.ok(
                orderService.getMyOrders()
        );
    }


    // ==========================================
    // CUSTOMER - MY ORDER BY ID
    // ==========================================

    @GetMapping("/my/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Order> getMyOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.getMyOrderById(id)
        );
    }


    // ==========================================
    // GET ORDER BY ID
    // SALES + ADMIN
    // ==========================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }


    // ==========================================
    // UPDATE ORDER
    // SALES + ADMIN
    // ==========================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SALES', 'ADMIN')")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @RequestBody Order order) {

        return ResponseEntity.ok(
                orderService.updateOrder(
                        id,
                        order
                )
        );
    }


    // ==========================================
    // DELETE ORDER
    // ADMIN ONLY
    // ==========================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
    

}