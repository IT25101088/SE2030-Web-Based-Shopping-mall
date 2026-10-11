package com.sliit.se2030.mall.user.dto;

import com.sliit.se2030.mall.support.entity.Inquiry;

import java.util.List;

/**
 * Everything the mall admin dashboard shows, gathered by DashboardService.
 * A plain class with getters for the same reason as CustomerDashboard.
 */
public class EmployeeDashboard {

    private final long shopsToApprove;
    private final long inquiriesWaiting;
    private final long inquiriesWithShops;
    // The oldest few help requests waiting on the mall team, for the "Needs you next" list.
    private final List<Inquiry> nextInquiries;
    private final long flaggedProducts;

    public EmployeeDashboard(long shopsToApprove, long inquiriesWaiting, long inquiriesWithShops,
                             List<Inquiry> nextInquiries, long flaggedProducts) {
        this.shopsToApprove = shopsToApprove;
        this.inquiriesWaiting = inquiriesWaiting;
        this.inquiriesWithShops = inquiriesWithShops;
        this.nextInquiries = nextInquiries;
        this.flaggedProducts = flaggedProducts;
    }

    public long getShopsToApprove() {
        return shopsToApprove;
    }

    public long getInquiriesWaiting() {
        return inquiriesWaiting;
    }

    public long getInquiriesWithShops() {
        return inquiriesWithShops;
    }

    public List<Inquiry> getNextInquiries() {
        return nextInquiries;
    }

    public long getFlaggedProducts() {
        return flaggedProducts;
    }
}
