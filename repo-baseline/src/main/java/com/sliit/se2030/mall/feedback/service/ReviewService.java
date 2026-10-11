package com.sliit.se2030.mall.feedback.service;

import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.feedback.dto.ReviewForm;
import com.sliit.se2030.mall.feedback.entity.Review;
import com.sliit.se2030.mall.feedback.repository.ReviewRepository;
import com.sliit.se2030.mall.order.entity.OrderItem;
import com.sliit.se2030.mall.order.entity.OrderStatus;
import com.sliit.se2030.mall.order.repository.OrderItemRepository;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Product Feedback & Reputation Analytics module. submitReview() enforces
 * verified-purchase-only reviews: the OrderItem must belong to the current
 * customer, be DELIVERED, and not already be reviewed.
 */
@Service
public class ReviewService {

    private static final double LOW_RATING_THRESHOLD = 2.5;

    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final CurrentUserProvider currentUserProvider;

    public ReviewService(ReviewRepository reviewRepository, OrderItemRepository orderItemRepository,
                          CustomerRepository customerRepository, CurrentUserProvider currentUserProvider) {
        this.reviewRepository = reviewRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    // Used by the review form's GET (to show what's being reviewed) and by
    // submitReview() (to re-validate on POST) -- ownership is checked either way.
    public OrderItem getReviewableOrderItem(Long orderItemId) {
        // TODO: implement getReviewableOrderItem -- see your NOTES.md,
        // "ReviewService.getReviewableOrderItem()". Load the OrderItem, throw
        // AccessDeniedForResourceException if it doesn't belong to the current
        // customer. Used by both the review form's GET and submitReview()'s POST.
        throw new UnsupportedOperationException("TODO: implement getReviewableOrderItem()");
    }

    @Transactional
    public Review submitReview(ReviewForm form) {
        // TODO: implement submitReview -- see your NOTES.md,
        // "ReviewService.submitReview()" for the full verified-purchase-only chain:
        // (1) getReviewableOrderItem(); (2) require status == DELIVERED;
        // (3) reject if already reviewed (existsByCustomer_IdAndVerifiedOrderItem_Id);
        // (4) save the Review; (5) recomputeFlagForProduct(orderItem.getProduct()).
        throw new UnsupportedOperationException("TODO: implement submitReview()");
    }

    public List<Review> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProduct_Id(productId);
    }

    public List<Review> getReviewsForCurrentMerchant() {
        Long merchantId = currentUserProvider.getCurrentUserId();
        return reviewRepository.findByProduct_Merchant_Id(merchantId);
    }

    public Double getAverageRating(Long productId) {
        return reviewRepository.findAverageRatingForProduct(productId);
    }

    public Map<Integer, Long> getRatingDistribution(Long productId) {
        // TODO: implement getRatingDistribution -- see your NOTES.md,
        // "ReviewService.getRatingDistribution()". Build a 1..5 -> count map,
        // always including all five keys (even 0 counts) so the JSP can render a
        // full bar chart without missing bars.
        throw new UnsupportedOperationException("TODO: implement getRatingDistribution()");
    }

    // Recomputed on every new review (can flag AND un-flag) rather than a
    // one-way ratchet -- a product's average can recover after a bad review.
    private void recomputeFlagForProduct(Product product) {
        // TODO: implement recomputeFlagForProduct -- see your NOTES.md,
        // "ReviewService.recomputeFlagForProduct()". Recompute the product's
        // average rating and set flaggedForReview = average < LOW_RATING_THRESHOLD.
        throw new UnsupportedOperationException("TODO: implement recomputeFlagForProduct()");
    }
}
