package com.sliit.se2030.mall.catalog.repository;

import com.sliit.se2030.mall.catalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String keyword);

    // The underscore lets Spring Data traverse a relationship: "Category_Id"
    // means "the id of this product's category", not a field literally
    // named categoryId. Same idea for Merchant_Id below.
    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByMerchant_Id(Long merchantId);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByActiveTrue();

    // Powers the platform employee's flagged-products view (feedback module
    // sets this flag when a product's average rating drops below threshold).
    List<Product> findByFlaggedForReviewTrue();
}
