package com.SSE.Website.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SSE.Website.GlobalExceptionHandler.ResourceNotFoundException;
import com.SSE.Website.entity.Order;
import com.SSE.Website.entity.OrderItem;
import com.SSE.Website.entity.Product;
import com.SSE.Website.repository.OrderItemRepository;
import com.SSE.Website.repository.OrderRepository;
import com.SSE.Website.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;


    // ==========================================
    // CREATE ORDER ITEM
    // ==========================================

    public OrderItem createOrderItem(
            OrderItem orderItem) {

        if (orderItem.getOrder() == null
                || orderItem.getOrder().getId() == null) {

            throw new RuntimeException(
                    "Order is required"
            );
        }

        if (orderItem.getProduct() == null
                || orderItem.getProduct().getId() == null) {

            throw new RuntimeException(
                    "Product is required"
            );
        }


        Long orderId =
                orderItem.getOrder().getId();

        Long productId =
                orderItem.getProduct().getId();


        // Find Order

        Order order =
                orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));


        // Find Product

        Product product =
                productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        ));


        orderItem.setOrder(order);

        orderItem.setProduct(product);


        // ======================================
        // CALCULATE SUBTOTAL
        // ======================================

        if (orderItem.getQuantity() == null
                || orderItem.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than 0"
            );
        }

        if (orderItem.getUnitPrice() == null
                || orderItem.getUnitPrice() < 0) {

            throw new RuntimeException(
                    "Unit price must be valid"
            );
        }


        double subtotal =
                orderItem.getQuantity()
                * orderItem.getUnitPrice();

        orderItem.setSubtotal(subtotal);


        OrderItem savedItem =
                orderItemRepository.save(orderItem);


        // Recalculate Order total

        updateOrderTotal(orderId);


        return savedItem;
    }


    // ==========================================
    // UPDATE ORDER TOTAL
    // ==========================================

    private void updateOrderTotal(Long orderId) {

        List<OrderItem> items =
                orderItemRepository.findByOrderId(orderId);


        double total = 0.0;


        for (OrderItem item : items) {

            if (item.getSubtotal() != null) {

                total += item.getSubtotal();
            }
        }


        Order order =
                orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));


        order.setTotalAmount(total);

        orderRepository.save(order);
    }


    // ==========================================
    // GET ALL ORDER ITEMS
    // ==========================================

    public List<OrderItem> getAllOrderItems() {

        return orderItemRepository.findAll();
    }


    // ==========================================
    // GET ORDER ITEM BY ID
    // ==========================================

    public OrderItem getOrderItemById(Long id) {

        return orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order item not found"
                        ));
    }


    // ==========================================
    // GET ITEMS BY ORDER ID
    // ==========================================

    public List<OrderItem> getItemsByOrderId(
            Long orderId) {

        return orderItemRepository
                .findByOrderId(orderId);
    }


    // ==========================================
    // UPDATE ORDER ITEM
    // ==========================================

    public OrderItem updateOrderItem(
            Long id,
            OrderItem orderItem) {

        OrderItem existing =
                getOrderItemById(id);


        Long oldOrderId =
                existing.getOrder().getId();


        // ======================================
        // QUANTITY
        // ======================================

        if (orderItem.getQuantity() != null) {

            if (orderItem.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Quantity must be greater than 0"
                );
            }

            existing.setQuantity(
                    orderItem.getQuantity()
            );
        }


        // ======================================
        // UNIT PRICE
        // ======================================

        if (orderItem.getUnitPrice() != null) {

            if (orderItem.getUnitPrice() < 0) {

                throw new RuntimeException(
                        "Unit price must be valid"
                );
            }

            existing.setUnitPrice(
                    orderItem.getUnitPrice()
            );
        }


        // ======================================
        // UPDATE ORDER
        // ======================================

        Long currentOrderId = oldOrderId;

        if (orderItem.getOrder() != null
                && orderItem.getOrder().getId() != null) {

            Long orderId =
                    orderItem.getOrder().getId();


            Order order =
                    orderRepository.findById(orderId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Order not found"
                            ));


            existing.setOrder(order);

            currentOrderId = orderId;
        }


        // ======================================
        // UPDATE PRODUCT
        // ======================================

        if (orderItem.getProduct() != null
                && orderItem.getProduct().getId() != null) {

            Long productId =
                    orderItem.getProduct().getId();


            Product product =
                    productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found"
                            ));


            existing.setProduct(product);
        }


        // ======================================
        // RECALCULATE SUBTOTAL
        // ======================================

        if (existing.getQuantity() != null
                && existing.getUnitPrice() != null) {

            existing.setSubtotal(
                    existing.getQuantity()
                    * existing.getUnitPrice()
            );
        }


        OrderItem updatedItem =
                orderItemRepository.save(existing);


        // Update current order total

        updateOrderTotal(currentOrderId);


        // If item moved to another order,
        // update old order total too.

        if (!oldOrderId.equals(currentOrderId)) {

            updateOrderTotal(oldOrderId);
        }


        return updatedItem;
    }


    // ==========================================
    // DELETE ORDER ITEM
    // ==========================================

    public void deleteOrderItem(Long id) {

        OrderItem existing =
                getOrderItemById(id);


        Long orderId =
                existing.getOrder().getId();


        orderItemRepository.delete(existing);


        // Recalculate order total

        updateOrderTotal(orderId);
    }

}