package com.sliit.se2030.mall.support.observer;

/**
 * The things that can happen to an inquiry. InquiryService announces each one
 * through InquiryEventPublisher, and every InquiryObserver hears about it.
 */
public enum InquiryEvent {
    SUBMITTED,
    EMPLOYEE_REPLIED,
    FORWARDED_TO_SHOP,
    TAKEN_BACK_FROM_SHOP,
    SHOP_REPLIED,
    RESOLVED
}
