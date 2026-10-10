package com.sliit.se2030.mall.feedback.controller;

import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.feedback.dto.MerchantResponseForm;
import com.sliit.se2030.mall.feedback.entity.MerchantResponse;
import com.sliit.se2030.mall.feedback.entity.Review;
import com.sliit.se2030.mall.feedback.service.MerchantResponseService;
import com.sliit.se2030.mall.feedback.service.ReputationService;
import com.sliit.se2030.mall.feedback.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// "/merchant/**" already restricted to ROLE_MERCHANT by SecurityConfig.
@Controller
@RequestMapping("/merchant/feedback")
public class MerchantFeedbackController {

    private final ReputationService reputationService;
    private final MerchantResponseService merchantResponseService;
    private final ReviewService reviewService;
    private final CurrentUserProvider currentUserProvider;

    public MerchantFeedbackController(ReputationService reputationService,
                                       MerchantResponseService merchantResponseService,
                                       ReviewService reviewService,
                                       CurrentUserProvider currentUserProvider) {
        this.reputationService = reputationService;
        this.merchantResponseService = merchantResponseService;
        this.reviewService = reviewService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    public String dashboard(Model model) {
        Long merchantId = currentUserProvider.getCurrentUserId();
        List<Review> reviews = reviewService.getReviewsForCurrentMerchant();

        // Pre-fetched into a plain Map (reviewId -> response), rather than handing
        // the JSP a service to call -- keeps EL simple (${responses[review.id]})
        // and avoids the JSP reasoning about an Optional.
        Map<Long, MerchantResponse> responses = new HashMap<>();
        for (Review review : reviews) {
            merchantResponseService.findResponse(review.getId()).ifPresent(r -> responses.put(review.getId(), r));
        }

        model.addAttribute("reputationScore", reputationService.getMerchantReputationScore(merchantId));
        model.addAttribute("reviews", reviews);
        model.addAttribute("responses", responses);
        return "feedback/merchant-feedback-dashboard";
    }

    @PostMapping("/{reviewId}/respond")
    public String respond(@PathVariable Long reviewId, @ModelAttribute MerchantResponseForm form) {
        merchantResponseService.respondToReview(reviewId, form);
        return "redirect:/merchant/feedback";
    }
}
