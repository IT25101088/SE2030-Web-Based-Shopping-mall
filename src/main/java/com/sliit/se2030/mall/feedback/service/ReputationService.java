package com.sliit.se2030.mall.feedback.service;

import com.sliit.se2030.mall.feedback.entity.Review;
import com.sliit.se2030.mall.feedback.rating.RatingStrategy;
import com.sliit.se2030.mall.feedback.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Strategy pattern -- the Context. Works out product and shop ratings with
 * whichever RatingStrategy mall.rating.strategy names; switching strategy is
 * a one-line change in application.properties, not a code change here.
 */
@Service
public class ReputationService {

    private final ReviewRepository reviewRepository;
    private final RatingStrategy ratingStrategy;

    // Spring passes in every RatingStrategy bean; we keep the configured one.
    // An unknown name stops the app at startup instead of failing on the first review.
    public ReputationService(ReviewRepository reviewRepository, List<RatingStrategy> strategies,
                             @Value("${mall.rating.strategy:simple}") String strategyName) {
        this.reviewRepository = reviewRepository;
        this.ratingStrategy = strategies.stream()
                .filter(strategy -> strategy.name().equals(strategyName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown mall.rating.strategy: " + strategyName));
    }

    // 0.0 (rather than null) when a merchant has no reviews yet -- simpler for
    // JSPs to render directly without a null check.
    public double getMerchantReputationScore(Long merchantId) {
        List<Review> reviews = reviewRepository.findByProduct_Merchant_Id(merchantId);
        return reviews.isEmpty() ? 0.0 : ratingStrategy.score(reviews);
    }

    // null when the product has no reviews yet.
    public Double getProductRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProduct_Id(productId);
        return reviews.isEmpty() ? null : ratingStrategy.score(reviews);
    }
}
