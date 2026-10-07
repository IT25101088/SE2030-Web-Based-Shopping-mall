package com.sliit.se2030.mall.common;

import com.sliit.se2030.mall.security.AppUserPrincipal;
import com.sliit.se2030.mall.user.entity.Role;
import com.sliit.se2030.mall.user.service.MerchantVerificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * The header is shared by every page, so the "new pending shops" badge needs
 * its number on every page too. A @ModelAttribute method in a @ControllerAdvice
 * runs before every controller method and adds its result to the model, so
 * no single controller has to remember to do it.
 */
@ControllerAdvice
public class AdminNotificationAdvice {

    private final MerchantVerificationService merchantVerificationService;

    public AdminNotificationAdvice(MerchantVerificationService merchantVerificationService) {
        this.merchantVerificationService = merchantVerificationService;
    }

    // Null for everyone except platform employees, so the header shows nothing for them.
    @ModelAttribute("unseenPendingShops")
    public Long unseenPendingShops(@AuthenticationPrincipal AppUserPrincipal principal) {
        if (principal == null || principal.getUser().getRole() != Role.PLATFORM_EMPLOYEE) {
            return null;
        }
        return merchantVerificationService.countUnseenPending(principal.getId());
    }
}
