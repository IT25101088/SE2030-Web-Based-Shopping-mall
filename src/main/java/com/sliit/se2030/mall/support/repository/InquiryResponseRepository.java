package com.sliit.se2030.mall.support.repository;

import com.sliit.se2030.mall.support.entity.InquiryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryResponseRepository extends JpaRepository<InquiryResponse, Long> {

    List<InquiryResponse> findByInquiry_Id(Long inquiryId);
}
