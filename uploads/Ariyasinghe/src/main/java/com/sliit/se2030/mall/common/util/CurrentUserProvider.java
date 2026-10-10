package com.sliit.se2030.mall.common.util;

import com.sliit.se2030.mall.security.AppUserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Lets service-layer code ask "who's logged in right now" without every
 * controller having to manually pull @AuthenticationPrincipal and pass an
 * id down as a method parameter. SecurityContextHolder is Spring Security's
 * own static access point to the current request's authentication -- safe
 * to call from anywhere on the request thread, not just controllers.
 *
 * Every module that needs to enforce "you can only touch your own data"
 * (merchant profile, merchant's own products/orders, etc.) should go
 * through this rather than trusting an id supplied by the client.
 */
@Component
public class CurrentUserProvider {

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUserPrincipal appUserPrincipal) {
            return appUserPrincipal.getId();
        }
        throw new IllegalStateException("No authenticated AppUserPrincipal in the current security context");
    }
}
