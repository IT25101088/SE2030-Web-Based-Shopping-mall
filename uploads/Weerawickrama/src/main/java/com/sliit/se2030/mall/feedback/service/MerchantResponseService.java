package com.sliit.se2030.mall.feedback.service;

import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.feedback.dto.MerchantResponseForm;
import com.sliit.se2030.mall.feedback.entity.MerchantResponse;
import com.sliit.se2030.mall.feedback.entity.Review;
import com.sliit.se2030.mall.feedback.repository.MerchantResponseRepository;
import com.sliit.se2030.mall.feedback.repository.ReviewRepository;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MerchantResponseService {

    private final MerchantResponseRepository merchantResponseRepository;
    private final ReviewRepository reviewRepository;
    private final MerchantRepository merchantRepository;
    private final CurrentUserProvider currentUserProvider;

    public MerchantResponseService(MerchantResponseRepository merchantResponseRepository,
                                    ReviewRepository reviewRepository, MerchantRepository merchantRepository,
                                    CurrentUserProvider currentUserProvider) {
        this.merchantResponseRepository = merchantResponseRepository;
        this.reviewRepository = reviewRepository;
        this.merchantRepository = merchantRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public void respondToReview(Long reviewId, MerchantResponseForm form) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + reviewId));
        Long merchantId = currentUserProvider.getCurrentUserId();
        if (!review.getProduct().getMerchant().getId().equals(merchantId)) {
            throw new AccessDeniedForResourceException("This review is not for one of your products.");
        }
        if (merchantResponseRepository.findByReview_Id(reviewId).isPresent()) {
            throw new BusinessRuleViolationException("You have already responded to this review.");
        }
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found: " + merchantId));

        merchantResponseRepository.save(new MerchantResponse(review, form.getResponseText(), merchant));
    }

    public Optional<MerchantResponse> findResponse(Long reviewId) {
        return merchantResponseRepository.findByReview_Id(reviewId);
    }
}
