package com.sliit.se2030.mall.order.controller;

import com.sliit.se2030.mall.cart.service.CartService;
import com.sliit.se2030.mall.order.dto.CheckoutForm;
import com.sliit.se2030.mall.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// "/checkout/**" already restricted to ROLE_CUSTOMER by SecurityConfig.
@Controller
public class CheckoutController {

    private final OrderService orderService;
    private final CartService cartService;

    public CheckoutController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        var cart = cartService.getOrCreateCartForCurrentCustomer();
        model.addAttribute("form", new CheckoutForm());
        model.addAttribute("items", cartService.getItems(cart));
        model.addAttribute("total", cartService.calculateTotal(cart));
        return "order/checkout";
    }

    @PostMapping("/checkout")
    public String submitCheckout(@Valid @ModelAttribute("form") CheckoutForm form, BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            var cart = cartService.getOrCreateCartForCurrentCustomer();
            model.addAttribute("items", cartService.getItems(cart));
            model.addAttribute("total", cartService.calculateTotal(cart));
            return "order/checkout";
        }
        var order = orderService.checkout(form);
        model.addAttribute("order", order);
        model.addAttribute("items", orderService.getItemsForOrder(order.getId()));
        return "order/order-detail";
    }
}
