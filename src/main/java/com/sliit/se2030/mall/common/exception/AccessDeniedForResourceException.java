package com.sliit.se2030.mall.common.exception;

/**
 * Thrown when a logged-in user tries to act on data that isn't theirs --
 * e.g. a merchant editing another merchant's product by tampering with a URL id.
 * This is a business-rule ownership check, separate from Spring Security's
 * role-based checks, which only look at the URL pattern, not who owns the data.
 */
public class AccessDeniedForResourceException extends RuntimeException {

    public AccessDeniedForResourceException(String message) {
        super(message);
    }
}
