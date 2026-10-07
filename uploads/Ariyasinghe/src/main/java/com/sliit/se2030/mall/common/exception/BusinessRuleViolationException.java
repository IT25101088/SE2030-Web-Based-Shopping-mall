package com.sliit.se2030.mall.common.exception;

/**
 * Thrown when an action is technically well-formed (valid ids, valid role)
 * but breaks a domain rule -- e.g. reviewing a product you never purchased,
 * or approving a merchant that's already approved.
 * Handled by redirecting back with a flash error message, not a hard error page.
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
