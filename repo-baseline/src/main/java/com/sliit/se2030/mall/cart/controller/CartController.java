package com.sliit.se2030.mall.cart.controller;

import com.sliit.se2030.mall.cart.dto.AddToCartForm;
import com.sliit.se2030.mall.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// STUB -- "/cart/**" already restricted to ROLE_CUSTOMER by SecurityConfig.
@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model) {
        var cart = cartService.getOrCreateCartForCurrentCustomer();
        model.addAttribute("items", cartService.getItems(cart));
        model.addAttribute("total", cartService.calculateTotal(cart));
        return "cart/view-cart";
    }

    @PostMapping("/add")
    public String addItem(@Valid @ModelAttribute("form") AddToCartForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/catalog";
        }
        cartService.addItem(form.getProductId(), form.getQuantity());
        return "redirect:/cart";
    }

    @PostMapping("/{itemId}/update")
    public String updateQuantity(@PathVariable Long itemId, int quantity) {
        cartService.updateQuantity(itemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/{itemId}/remove")
    public String removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }
}
