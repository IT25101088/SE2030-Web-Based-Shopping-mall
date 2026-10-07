package com.sliit.se2030.mall.support.entity;

/**
 * AWAITING_REVIEW means a shop has answered a forwarded inquiry and handed it
 * back: the mall team now checks the shop's note, then resolves or replies.
 * Only a platform employee can move an inquiry to RESOLVED.
 */
public enum InquiryStatus {
    OPEN,
    IN_PROGRESS,
    AWAITING_REVIEW,
    RESOLVED
}
