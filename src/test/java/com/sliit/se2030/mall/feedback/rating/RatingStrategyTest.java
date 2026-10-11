package com.sliit.se2030.mall.feedback.rating;

import com.sliit.se2030.mall.feedback.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/** Plain unit test of the rating strategies. */
class RatingStrategyTest {

    // createdAt is normally set by JPA when the review is saved, so set it directly here.
    private static Review review(int rating, int daysAgo) {
        Review review = new Review(null, null, rating, "", null);
        ReflectionTestUtils.setField(review, "createdAt", Instant.now().minus(Duration.ofDays(daysAgo)));
        return review;
    }

    @Test
    void simpleAverage_countsEveryReviewTheSame() {
        List<Review> reviews = List.of(review(5, 0), review(1, 360));

        assertThat(new SimpleAverageRating().score(reviews)).isEqualTo(3.0);
    }

    @Test
    void recencyWeighted_favoursTheNewerReview() {
        // Weights 1 and 0.25 (two half-lives old): (5*1 + 1*0.25) / 1.25 = 4.2
        List<Review> reviews = List.of(review(5, 0), review(1, 360));

        assertThat(new RecencyWeightedRating().score(reviews)).isCloseTo(4.2, within(0.01));
    }
}
