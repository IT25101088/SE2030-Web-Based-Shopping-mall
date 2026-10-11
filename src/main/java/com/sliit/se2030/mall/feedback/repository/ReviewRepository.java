package com.sliit.se2030.mall.feedback.repository;

import com.sliit.se2030.mall.feedback.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct_Id(Long productId);

    List<Review> findByProduct_Merchant_Id(Long merchantId);

    boolean existsByCustomer_IdAndVerifiedOrderItem_Id(Long customerId, Long orderItemId);
}
