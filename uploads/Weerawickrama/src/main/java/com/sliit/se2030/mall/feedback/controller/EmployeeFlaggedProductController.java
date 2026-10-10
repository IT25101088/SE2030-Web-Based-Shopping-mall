package com.sliit.se2030.mall.feedback.controller;

import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// STUB -- "/employee/**" already restricted to ROLE_PLATFORM_EMPLOYEE by SecurityConfig.
@Controller
public class EmployeeFlaggedProductController {

    private final ProductRepository productRepository;

    public EmployeeFlaggedProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/employee/flagged-products")
    public String listFlagged(Model model) {
        model.addAttribute("products", productRepository.findByFlaggedForReviewTrue());
        return "feedback/flagged-products";
    }
}
