package com.sliit.se2030.mall.feedback.service;

import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.feedback.dto.MerchantResponseForm;
import com.sliit.se2030.mall.feedback.entity.Review;
import com.sliit.se2030.mall.feedback.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantResponseService {

    private final ReviewRepository reviewRepository;
    private final CurrentUserProvider currentUserProvider;

    public MerchantResponseService(ReviewRepository reviewRepository, CurrentUserProvider currentUserProvider) {
        this.reviewRepository = reviewRepository;
        this.currentUserProvider = currentUserProvider;
    }

    // No explicit save() needed: the review is a managed entity inside this
    // transaction, so Hibernate writes the new reply columns on commit.
    @Transactional
    public void respondToReview(Long reviewId, MerchantResponseForm form) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + reviewId));
        Long merchantId = currentUserProvider.getCurrentUserId();
        if (!review.getProduct().getMerchant().getId().equals(merchantId)) {
            throw new AccessDeniedForResourceException("This review is not for one of your products.");
        }
        if (review.hasMerchantResponse()) {
            throw new BusinessRuleViolationException("You have already responded to this review.");
        }

        review.addMerchantResponse(form.getResponseText());
    }
}
