package com.sliit.se2030.mall.catalog.controller;

import com.sliit.se2030.mall.catalog.service.CategoryService;
import com.sliit.se2030.mall.catalog.service.ProductService;
import com.sliit.se2030.mall.feedback.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

// Public product browsing. "/catalog/**" is already permitAll in SecurityConfig.
@Controller
public class CatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public CatalogController(ProductService productService, CategoryService categoryService,
                              ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    // All filters are optional query params, e.g. /catalog?keyword=shoe&categoryId=2.
    // Leaving them off (or blank) means "don't filter on this."
    @GetMapping("/catalog")
    public String browse(@RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Long categoryId,
                          @RequestParam(required = false) BigDecimal minPrice,
                          @RequestParam(required = false) BigDecimal maxPrice,
                          Model model) {
        model.addAttribute("products", productService.browse(keyword, categoryId, minPrice, maxPrice));
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "catalog/browse";
    }

    @GetMapping("/catalog/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductDetail(id));
        model.addAttribute("reviews", reviewService.getReviewsForProduct(id));
        model.addAttribute("averageRating", reviewService.getAverageRating(id));
        return "catalog/product-detail";
    }
}
