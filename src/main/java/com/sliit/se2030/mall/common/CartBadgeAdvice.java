package com.sliit.se2030.mall.common;

import com.sliit.se2030.mall.cart.service.CartService;
import com.sliit.se2030.mall.security.AppUserPrincipal;
import com.sliit.se2030.mall.user.entity.Role;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Supplies the item count for the cart button in the shared header, the same
 * way AdminNotificationAdvice supplies the "new pending shops" badge: a
 * @ModelAttribute in a @ControllerAdvice runs before every controller method,
 * so every page has the number without each controller adding it.
 */
@ControllerAdvice
public class CartBadgeAdvice {

    private final CartService cartService;

    public CartBadgeAdvice(CartService cartService) {
        this.cartService = cartService;
    }

    // Null for everyone except customers -- only customers have a cart.
    @ModelAttribute("cartItemCount")
    public Integer cartItemCount(@AuthenticationPrincipal AppUserPrincipal principal) {
        if (principal == null || principal.getUser().getRole() != Role.CUSTOMER) {
            return null;
        }
        return cartService.countItemsForCurrentCustomer();
    }
}
