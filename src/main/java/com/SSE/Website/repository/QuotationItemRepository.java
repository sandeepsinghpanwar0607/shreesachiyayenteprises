package com.SSE.Website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SSE.Website.entity.QuotationItem;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {

    List<QuotationItem> findByQuotationId(Long quotationId);
}