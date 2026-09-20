package com.SSE.Website.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.Order;
import com.SSE.Website.entity.OrderItem;
import com.SSE.Website.entity.Quotation;
import com.SSE.Website.entity.QuotationItem;
import com.SSE.Website.entity.User;
import com.SSE.Website.repository.OrderItemRepository;
import com.SSE.Website.repository.OrderRepository;
import com.SSE.Website.repository.QuotationItemRepository;
import com.SSE.Website.repository.QuotationRepository;
import com.SSE.Website.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final QuotationRepository quotationRepository;
    private final UserRepository userRepository;

    private final QuotationItemRepository quotationItemRepository;
    private final OrderItemRepository orderItemRepository;


    // ==========================================
    // CREATE ORDER
    // ONLY ACCEPTED QUOTATION
    // ==========================================

    public Order createOrder(Order order) {

        if (order.getQuotation() == null
                || order.getQuotation().getId() == null) {

            throw new RuntimeException(
                    "Quotation is required to create an order"
            );
        }

        Long quotationId =
                order.getQuotation().getId();

        Quotation quotation =
                quotationRepository.findById(quotationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Quotation not found"
                        ));

        if (!"ACCEPTED".equalsIgnoreCase(
                quotation.getStatus())) {

            throw new RuntimeException(
                    "Order can only be created from an ACCEPTED quotation"
            );
        }

        order.setQuotation(quotation);

        order.setCreatedAt(
                LocalDateTime.now()
        );

        order.setStatus("PROCESSING");

        order.setTotalAmount(
                quotation.getTotalAmount()
        );


        // ==========================================
        // SAVE ORDER
        // ==========================================

        Order savedOrder =
                orderRepository.save(order);


        // ==========================================
        // CREATE ORDER ITEMS
        // FROM QUOTATION ITEMS
        // ==========================================

        List<QuotationItem> quotationItems =
                quotationItemRepository
                        .findByQuotationId(
                                quotation.getId()
                        );

        for (QuotationItem quotationItem : quotationItems) {

            OrderItem orderItem =
                    OrderItem.builder()
                            .quantity(
                                    quotationItem.getQuantity()
                            )
                            .unitPrice(
                                    quotationItem.getUnitPrice()
                            )
                            .subtotal(
                                    quotationItem.getSubtotal()
                            )
                            .order(savedOrder)
                            .product(
                                    quotationItem.getProduct()
                            )
                            .build();

            orderItemRepository.save(orderItem);
        }


        return savedOrder;
    }


    // ==========================================
    // GET ALL ORDERS
    // SALES / ADMIN
    // ==========================================

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }


    // ==========================================
    // GET MY ORDERS
    // CUSTOMER ONLY
    // ==========================================

    public List<Order> getMyOrders() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication.getName() == null) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String email =
                authentication.getName();

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged-in user not found"
                        ));

        return orderRepository
                .findByQuotationInquiryUserId(
                        user.getId()
                );
    }


    // ==========================================
    // GET ORDER BY ID
    // SALES / ADMIN
    // ==========================================

    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));
    }


    // ==========================================
    // GET MY ORDER BY ID
    // CUSTOMER ONLY
    // ==========================================

    public Order getMyOrderById(Long id) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication.getName() == null) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String email =
                authentication.getName();

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logged-in user not found"
                        ));

        Order order =
                orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));

        if (order.getQuotation() == null
                || order.getQuotation().getInquiry() == null
                || order.getQuotation().getInquiry().getUser() == null
                || !order.getQuotation()
                        .getInquiry()
                        .getUser()
                        .getId()
                        .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to access this order"
            );
        }

        return order;
    }


    // ==========================================
    // UPDATE ORDER
    // SALES / ADMIN
    // ==========================================

    public Order updateOrder(
            Long id,
            Order order) {

        Order existing =
                getOrderById(id);


        // ======================================
        // ORDER NUMBER
        // ======================================

        if (order.getOrderNumber() != null) {

            existing.setOrderNumber(
                    order.getOrderNumber()
            );
        }


        // ======================================
        // NOTES
        // ======================================

        if (order.getNotes() != null) {

            existing.setNotes(
                    order.getNotes()
            );
        }


        // ======================================
        // STATUS
        // STRICT FLOW
        // PROCESSING → SHIPPED → DELIVERED
        // ======================================

        if (order.getStatus() != null) {

            String currentStatus =
                    existing.getStatus();

            String newStatus =
                    order.getStatus().toUpperCase();


            if (!newStatus.equals("PROCESSING")
                    && !newStatus.equals("SHIPPED")
                    && !newStatus.equals("DELIVERED")) {

                throw new RuntimeException(
                        "Invalid order status. "
                        + "Allowed: PROCESSING, SHIPPED, DELIVERED"
                );
            }


            if ("PROCESSING".equals(currentStatus)
                    && !"SHIPPED".equals(newStatus)) {

                throw new RuntimeException(
                        "Order status can only move "
                        + "from PROCESSING to SHIPPED"
                );
            }


            if ("SHIPPED".equals(currentStatus)
                    && !"DELIVERED".equals(newStatus)) {

                throw new RuntimeException(
                        "Order status can only move "
                        + "from SHIPPED to DELIVERED"
                );
            }


            if ("DELIVERED".equals(currentStatus)) {

                throw new RuntimeException(
                        "Delivered order status cannot be changed"
                );
            }


            existing.setStatus(newStatus);
        }


        // ======================================
        // UPDATE QUOTATION
        // ONLY ACCEPTED QUOTATION
        // ======================================

        if (order.getQuotation() != null
                && order.getQuotation().getId() != null) {

            Long quotationId =
                    order.getQuotation().getId();

            Quotation quotation =
                    quotationRepository.findById(
                            quotationId
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Quotation not found"
                            ));


            if (!"ACCEPTED".equalsIgnoreCase(
                    quotation.getStatus())) {

                throw new RuntimeException(
                        "Order can only be linked "
                        + "to an ACCEPTED quotation"
                );
            }


            existing.setQuotation(
                    quotation
            );

            existing.setTotalAmount(
                    quotation.getTotalAmount()
            );
        }


        return orderRepository.save(existing);
    }


    // ==========================================
    // DELETE ORDER
    // ADMIN ONLY
    // ==========================================
    @Transactional
    public void deleteOrder(Long id) {

        Order existing = getOrderById(id);

        // First delete child OrderItems directly from database
        orderItemRepository.deleteByOrderId(id);

        // Then delete parent Order
        orderRepository.delete(existing);
    }

}