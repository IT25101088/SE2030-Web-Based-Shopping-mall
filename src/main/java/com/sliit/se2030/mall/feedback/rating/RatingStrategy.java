package com.sliit.se2030.mall.feedback.rating;

import com.sliit.se2030.mall.feedback.entity.Review;

import java.util.List;

/**
 * Strategy pattern -- the Strategy interface. A way of turning a list of
 * 1-5 star reviews into one score. ReputationService uses whichever strategy
 * mall.rating.strategy names, for product ratings, shop reputation and the
 * low-rating flag alike.
 */
public interface RatingStrategy {

    // The value used in mall.rating.strategy, e.g. "simple".
    String name();

    // Only called with at least one review.
    double score(List<Review> reviews);
}
