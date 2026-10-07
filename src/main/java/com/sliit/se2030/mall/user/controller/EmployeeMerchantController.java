package com.sliit.se2030.mall.user.controller;

import com.sliit.se2030.mall.security.AppUserPrincipal;
import com.sliit.se2030.mall.user.service.MerchantVerificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

// Every URL under here is already restricted to ROLE_PLATFORM_EMPLOYEE by
// SecurityConfig's "/employee/**" rule -- nothing extra needed here for that.
@Controller
@RequestMapping("/employee/merchants")
public class EmployeeMerchantController {

    private final MerchantVerificationService merchantVerificationService;

    public EmployeeMerchantController(MerchantVerificationService merchantVerificationService) {
        this.merchantVerificationService = merchantVerificationService;
    }

    // "Shops": every shop in the mall, whatever its status.
    @GetMapping
    public String allShops(Model model) {
        model.addAttribute("merchants", merchantVerificationService.listAll());
        return "user/employee-shop-list";
    }

    @GetMapping("/pending")
    public String pendingMerchants(@AuthenticationPrincipal AppUserPrincipal principal, Model model) {
        // Opening this page counts as "seen", which clears this employee's badge.
        // We keep the previous time so the page can still mark which shops are new.
        Instant previouslySeenAt = merchantVerificationService.markPendingSeen(principal.getId());
        // The header badge was worked out before this request marked the list as
        // seen, so overwrite it here, or the badge would show for one extra page.
        model.addAttribute("unseenPendingShops", 0L);
        model.addAttribute("previouslySeenAt", previouslySeenAt);
        model.addAttribute("merchants", merchantVerificationService.listPending());
        return "user/employee-merchant-list";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, @AuthenticationPrincipal AppUserPrincipal principal) {
        merchantVerificationService.approveMerchant(id, principal.getId());
        return "redirect:/employee/merchants/pending";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, @AuthenticationPrincipal AppUserPrincipal principal) {
        merchantVerificationService.rejectMerchant(id, principal.getId());
        return "redirect:/employee/merchants/pending";
    }

    @PostMapping("/{id}/suspend")
    public String suspend(@PathVariable Long id, @AuthenticationPrincipal AppUserPrincipal principal) {
        merchantVerificationService.suspendMerchant(id, principal.getId());
        return "redirect:/employee/merchants";
    }

    @PostMapping("/{id}/reinstate")
    public String reinstate(@PathVariable Long id, @AuthenticationPrincipal AppUserPrincipal principal) {
        merchantVerificationService.reinstateMerchant(id, principal.getId());
        return "redirect:/employee/merchants";
    }
}
