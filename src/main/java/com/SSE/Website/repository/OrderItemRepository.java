package com.SSE.Website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SSE.Website.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Long orderId);
}