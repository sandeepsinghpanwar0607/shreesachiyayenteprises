package com.SSE.Website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SSE.Website.entity.InquiryItem;

@Repository
public interface InquiryItemRepository extends JpaRepository<InquiryItem, Long> {

    List<InquiryItem> findByInquiryId(Long inquiryId);
}