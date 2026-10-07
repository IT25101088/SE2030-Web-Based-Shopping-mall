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
        // TODO: implement getMerchantReputationScore -- see your NOTES.md,
        // "ReputationService.getMerchantReputationScore()". Return 0.0 (never null)
        // when the merchant has no reviews yet, so the JSP can render it directly.
        throw new UnsupportedOperationException("TODO: implement getMerchantReputationScore()");
    }
}
