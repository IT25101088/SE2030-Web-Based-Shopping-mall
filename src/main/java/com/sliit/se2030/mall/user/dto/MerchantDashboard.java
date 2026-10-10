package com.sliit.se2030.mall.user.dto;

import com.sliit.se2030.mall.order.entity.OrderItem;

import java.util.List;

/**
 * Everything the merchant dashboard shows, gathered by DashboardService.
 * A plain class with getters for the same reason as CustomerDashboard.
 */
public class MerchantDashboard {

    private final long ordersToSend;
    // The oldest few order items still to send, for the "Needs you next" list.
    private final List<OrderItem> nextOrders;
    private final long productCount;
    private final long hiddenProducts;
    private final long lowStockProducts;
    private final long openInquiries;
    // Null when the shop has no reviews yet, so the page can say so instead of showing 0.0.
    private final Double rating;

    public MerchantDashboard(long ordersToSend, List<OrderItem> nextOrders, long productCount, long hiddenProducts,
                             long lowStockProducts, long openInquiries, Double rating) {
        this.ordersToSend = ordersToSend;
        this.nextOrders = nextOrders;
        this.productCount = productCount;
        this.hiddenProducts = hiddenProducts;
        this.lowStockProducts = lowStockProducts;
        this.openInquiries = openInquiries;
        this.rating = rating;
    }

    public long getOrdersToSend() {
        return ordersToSend;
    }

    public List<OrderItem> getNextOrders() {
        return nextOrders;
    }

    public long getProductCount() {
        return productCount;
    }

    public long getHiddenProducts() {
        return hiddenProducts;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public long getOpenInquiries() {
        return openInquiries;
    }

    public Double getRating() {
        return rating;
    }
}
