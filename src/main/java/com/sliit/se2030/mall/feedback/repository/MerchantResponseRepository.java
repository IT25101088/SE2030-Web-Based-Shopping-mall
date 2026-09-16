package com.sliit.se2030.mall.feedback.repository;

import com.sliit.se2030.mall.feedback.entity.MerchantResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantResponseRepository extends JpaRepository<MerchantResponse, Long> {

    Optional<MerchantResponse> findByReview_Id(Long reviewId);
}
