package com.sliit.se2030.mall.feedback.rating;

import com.sliit.se2030.mall.feedback.entity.Review;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Strategy pattern -- a Concrete Strategy: newer reviews count for more, so a
 * shop that has improved isn't held back by old complaints. A review's weight
 * halves every HALF_LIFE_DAYS: today's counts 1, a 6-month-old one 0.5, a
 * year-old one 0.25.
 */
@Component
public class RecencyWeightedRating implements RatingStrategy {

    private static final double HALF_LIFE_DAYS = 180;

    @Override
    public String name() {
        return "recency-weighted";
    }

    @Override
    public double score(List<Review> reviews) {
        Instant now = Instant.now();
        double weightedSum = 0;
        double totalWeight = 0;
        for (Review review : reviews) {
            double ageDays = Math.max(0, Duration.between(review.getCreatedAt(), now).toHours() / 24.0);
            double weight = Math.pow(0.5, ageDays / HALF_LIFE_DAYS);
            weightedSum += weight * review.getRating();
            totalWeight += weight;
        }
        return totalWeight == 0 ? 0.0 : weightedSum / totalWeight;
    }
}
