package com.sliit.se2030.mall.feedback.repository;

import com.sliit.se2030.mall.feedback.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct_Id(Long productId);

    List<Review> findByProduct_Merchant_Id(Long merchantId);

    boolean existsByCustomer_IdAndVerifiedOrderItem_Id(Long customerId, Long orderItemId);

    // @Query lets us write JPQL directly when a derived method name would get
    // unwieldy -- this is an aggregate (AVG), which Spring Data can't derive
    // from a method name alone. JPQL looks like SQL but operates on entities
    // and their fields (e.g. "r.rating"), not raw table/column names.
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingForProduct(@Param("productId") Long productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.merchant.id = :merchantId")
    Double findAverageRatingForMerchant(@Param("merchantId") Long merchantId);
}
