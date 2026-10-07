package com.sliit.se2030.mall.user.entity;

/**
 * A merchant's approval state. Only APPROVED merchants should be able to
 * list products and fulfil orders -- enforced in the service layer, not here.
 */
public enum VerificationStatus {
    PENDING,
    APPROVED,
    SUSPENDED,
    REJECTED
}
