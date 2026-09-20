package com.SSE.Website.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.SSE.Website.entity.Quotation;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
	List<Quotation> findByInquiryUserId(Long userId);
}