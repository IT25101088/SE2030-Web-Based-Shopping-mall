package com.sliit.se2030.mall.catalog.controller;

import com.sliit.se2030.mall.catalog.dto.ProductForm;
import com.sliit.se2030.mall.catalog.service.CategoryService;
import com.sliit.se2030.mall.catalog.service.ProductService;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// STUB -- "/merchant/**" already restricted to ROLE_MERCHANT by SecurityConfig.
@Controller
@RequestMapping("/merchant/products")
public class MerchantProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public MerchantProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listOwnProducts(Model model) {
        model.addAttribute("products", productService.listOwnProducts());
        return "catalog/merchant-product-list";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("form", new ProductForm());
        model.addAttribute("categories", categoryService.listAll());
        return "catalog/merchant-product-form";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("form") ProductForm form, BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            model.addAttribute("categories", categoryService.listAll());
            return "catalog/merchant-product-form";
        }
        try {
            productService.createProduct(form);
        } catch (BusinessRuleViolationException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("categories", categoryService.listAll());
            return "catalog/merchant-product-form";
        }
        return "redirect:/merchant/products";
    }

    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        var product = productService.getProductDetail(id);
        ProductForm form = new ProductForm();
        form.setName(product.getName());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setStockQuantity(product.getStockQuantity());
        form.setImageUrl(product.getImageUrl());
        form.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);

        model.addAttribute("form", form);
        model.addAttribute("productId", id);
        model.addAttribute("categories", categoryService.listAll());
        return "catalog/merchant-product-form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id,
                                 @Valid @ModelAttribute("form") ProductForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            model.addAttribute("productId", id);
            model.addAttribute("categories", categoryService.listAll());
            return "catalog/merchant-product-form";
        }
        productService.updateProduct(id, form);
        return "redirect:/merchant/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/merchant/products";
    }
}
