package com.sliit.se2030.mall.order.controller;

import com.sliit.se2030.mall.order.entity.OrderStatus;
import com.sliit.se2030.mall.order.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * "/merchant/**" already restricted to ROLE_MERCHANT by SecurityConfig.
 * Status updates target one OrderItem, not a whole Order -- an order can span
 * multiple merchants, so each merchant only ever advances their own items.
 * See OrderService.recomputeOrderStatus() for how the order's overall status
 * is derived from these.
 */
@Controller
@RequestMapping("/merchant/orders")
public class MerchantOrderController {

    private final OrderService orderService;

    public MerchantOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOwnOrderItems(Model model) {
        model.addAttribute("orderItems", orderService.getOrderItemsForCurrentMerchant());
        model.addAttribute("statuses", OrderStatus.values());
        return "order/merchant-order-list";
    }

    @PostMapping("/items/{orderItemId}/status")
    public String updateItemStatus(@PathVariable Long orderItemId, @RequestParam OrderStatus status) {
        orderService.updateOrderItemStatus(orderItemId, status);
        return "redirect:/merchant/orders";
    }
}
