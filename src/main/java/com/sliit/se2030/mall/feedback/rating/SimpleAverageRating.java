package com.sliit.se2030.mall.feedback.rating;

import com.sliit.se2030.mall.feedback.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;

/** Strategy pattern -- a Concrete Strategy: every review counts the same. */
@Component
public class SimpleAverageRating implements RatingStrategy {

    @Override
    public String name() {
        return "simple";
    }

    @Override
    public double score(List<Review> reviews) {
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }
}
