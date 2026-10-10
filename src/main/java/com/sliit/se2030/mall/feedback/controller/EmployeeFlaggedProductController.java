package com.sliit.se2030.mall.feedback.controller;

import com.sliit.se2030.mall.catalog.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// "/employee/**" already restricted to ROLE_PLATFORM_EMPLOYEE by SecurityConfig.
@Controller
public class EmployeeFlaggedProductController {

    private final ProductService productService;

    public EmployeeFlaggedProductController(ProductService productService) {
        this.productService = productService;
    }

    // Two lists: products an employee hid by hand, and products whose rating
    // dropped below the limit (set automatically by ReviewService).
    @GetMapping("/employee/flagged-products")
    public String listFlagged(Model model) {
        model.addAttribute("adminFlagged", productService.listFlaggedByAdmin());
        model.addAttribute("products", productService.listFlaggedForLowRating());
        return "feedback/flagged-products";
    }

    @PostMapping("/employee/products/{id}/flag")
    public String flag(@PathVariable Long id, @RequestParam(required = false) String reason) {
        productService.flagProduct(id, reason);
        return "redirect:/catalog/" + id;
    }

    @PostMapping("/employee/products/{id}/unflag")
    public String unflag(@PathVariable Long id) {
        productService.unflagProduct(id);
        return "redirect:/employee/flagged-products";
    }
}
