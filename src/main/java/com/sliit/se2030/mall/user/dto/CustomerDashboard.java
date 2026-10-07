package com.sliit.se2030.mall.user.dto;

import com.sliit.se2030.mall.order.entity.Order;

import java.math.BigDecimal;

/**
 * Everything the customer home page shows, gathered by DashboardService.
 * Read-only: the page only displays these values, it never sends them back.
 *
 * A plain class with getters (not a Java record) because the JSP expression
 * language in our Tomcat version reads values through getXxx() methods.
 */
public class CustomerDashboard {

    private final int cartItemCount;
    private final BigDecimal cartTotal;
    private final long itemsOnTheWay;
    private final long openInquiries;
    // Null when the customer has never ordered anything.
    private final Order latestOrder;
    private final int latestOrderItemCount;

    public CustomerDashboard(int cartItemCount, BigDecimal cartTotal, long itemsOnTheWay, long openInquiries,
                             Order latestOrder, int latestOrderItemCount) {
        this.cartItemCount = cartItemCount;
        this.cartTotal = cartTotal;
        this.itemsOnTheWay = itemsOnTheWay;
        this.openInquiries = openInquiries;
        this.latestOrder = latestOrder;
        this.latestOrderItemCount = latestOrderItemCount;
    }

    public int getCartItemCount() {
        return cartItemCount;
    }

    public BigDecimal getCartTotal() {
        return cartTotal;
    }

    public long getItemsOnTheWay() {
        return itemsOnTheWay;
    }

    public long getOpenInquiries() {
        return openInquiries;
    }

    public Order getLatestOrder() {
        return latestOrder;
    }

    public int getLatestOrderItemCount() {
        return latestOrderItemCount;
    }
}
