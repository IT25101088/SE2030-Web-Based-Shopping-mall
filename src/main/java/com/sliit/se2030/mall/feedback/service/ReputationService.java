package com.sliit.se2030.mall.feedback.service;

import com.sliit.se2030.mall.feedback.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReputationService {

    private final ReviewRepository reviewRepository;

    public ReputationService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 0.0 (rather than null) when a merchant has no reviews yet -- simpler for
    // JSPs to render directly without a null check.
    public double getMerchantReputationScore(Long merchantId) {
        Double average = reviewRepository.findAverageRatingForMerchant(merchantId);
        return average != null ? average : 0.0;
    }
}
