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
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found: " + orderItemId));
        Long customerId = currentUserProvider.getCurrentUserId();
        if (!item.getOrder().getCustomer().getId().equals(customerId)) {
            throw new AccessDeniedForResourceException("This order item does not belong to you.");
        }
        return item;
    }

    @Transactional
    public Review submitReview(ReviewForm form) {
        OrderItem orderItem = getReviewableOrderItem(form.getOrderItemId());
        if (orderItem.getStatus() != OrderStatus.DELIVERED) {
            throw new BusinessRuleViolationException("You can only review items that have been delivered.");
        }

        Long customerId = currentUserProvider.getCurrentUserId();
        if (reviewRepository.existsByCustomer_IdAndVerifiedOrderItem_Id(customerId, orderItem.getId())) {
            throw new BusinessRuleViolationException("You have already reviewed this item.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        Review review = reviewRepository.save(
                new Review(customer, orderItem.getProduct(), form.getRating(), form.getComment(), orderItem));

        recomputeFlagForProduct(orderItem.getProduct());
        return review;
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
        Map<Integer, Long> distribution = new LinkedHashMap<>();
        for (int rating = 1; rating <= 5; rating++) {
            distribution.put(rating, 0L);
        }
        for (Review review : getReviewsForProduct(productId)) {
            distribution.merge(review.getRating(), 1L, Long::sum);
        }
        return distribution;
    }

    // Recomputed on every new review (can flag AND un-flag) rather than a
    // one-way ratchet -- a product's average can recover after a bad review.
    private void recomputeFlagForProduct(Product product) {
        Double average = reviewRepository.findAverageRatingForProduct(product.getId());
        product.setFlaggedForReview(average != null && average < LOW_RATING_THRESHOLD);
    }
}
