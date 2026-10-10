package com.sliit.se2030.mall.feedback.controller;

import com.sliit.se2030.mall.feedback.dto.ReviewForm;
import com.sliit.se2030.mall.feedback.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// "/reviews/submit/**" already restricted to ROLE_CUSTOMER by SecurityConfig.
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // "Leave a review" is only reachable from the customer's own order detail
    // page, for a DELIVERED order item -- ownership is still re-checked here
    // (getReviewableOrderItem) and again on submit, never trusted from the link alone.
    @GetMapping("/reviews/submit")
    public String reviewForm(@RequestParam Long orderItemId, Model model) {
        var orderItem = reviewService.getReviewableOrderItem(orderItemId);
        ReviewForm form = new ReviewForm();
        form.setOrderItemId(orderItemId);
        model.addAttribute("form", form);
        model.addAttribute("orderItem", orderItem);
        return "feedback/submit-review";
    }

    @PostMapping("/reviews/submit")
    public String submitReview(@Valid @ModelAttribute("form") ReviewForm form, BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            model.addAttribute("orderItem", reviewService.getReviewableOrderItem(form.getOrderItemId()));
            return "feedback/submit-review";
        }
        reviewService.submitReview(form);
        return "redirect:/orders";
    }
}
