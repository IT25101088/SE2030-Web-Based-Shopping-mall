package com.sliit.se2030.mall.user.entity;

/**
 * The three actor types in the system. Doubles as the Spring Security
 * authority name later (mapped to "ROLE_CUSTOMER" etc.) -- see AppUserDetailsService.
 */
public enum Role {
    CUSTOMER,
    MERCHANT,
    PLATFORM_EMPLOYEE
}
