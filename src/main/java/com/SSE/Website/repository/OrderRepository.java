package com.SSE.Website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SSE.Website.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByQuotationInquiryUserId(Long userId);

}