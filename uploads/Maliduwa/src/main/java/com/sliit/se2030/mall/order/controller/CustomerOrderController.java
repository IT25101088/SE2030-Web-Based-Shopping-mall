package com.sliit.se2030.mall.order.controller;

import com.sliit.se2030.mall.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

// STUB -- "/orders/**" already restricted to ROLE_CUSTOMER by SecurityConfig.
@Controller
@RequestMapping("/orders")
public class CustomerOrderController {

    private final OrderService orderService;

    public CustomerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String orderHistory(Model model) {
        model.addAttribute("orders", orderService.getOrderHistoryForCurrentCustomer());
        return "order/order-history";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        var order = orderService.getOrderDetail(id);
        model.addAttribute("order", order);
        model.addAttribute("items", orderService.getItemsForOrder(order.getId()));
        return "order/order-detail";
    }
}
