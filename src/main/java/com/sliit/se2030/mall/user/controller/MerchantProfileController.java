package com.sliit.se2030.mall.user.controller;

import com.sliit.se2030.mall.user.dto.ShopProfileForm;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.service.MerchantProfileService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// "/merchant/**" is already restricted to ROLE_MERCHANT by SecurityConfig.
@Controller
@RequestMapping("/merchant/profile")
public class MerchantProfileController {

    private final MerchantProfileService merchantProfileService;

    public MerchantProfileController(MerchantProfileService merchantProfileService) {
        this.merchantProfileService = merchantProfileService;
    }

    @GetMapping
    public String viewProfile(Model model) {
        Merchant merchant = merchantProfileService.getOwnProfile();
        model.addAttribute("form", toForm(merchant));
        model.addAttribute("verificationStatus", merchant.getVerificationStatus());
        return "user/merchant-profile";
    }

    @PostMapping
    public String updateProfile(@Valid @ModelAttribute("form") ShopProfileForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            model.addAttribute("verificationStatus", merchantProfileService.getOwnProfile().getVerificationStatus());
            return "user/merchant-profile";
        }
        merchantProfileService.updateShopProfile(form);
        return "redirect:/merchant/profile?updated";
    }

    private ShopProfileForm toForm(Merchant merchant) {
        ShopProfileForm form = new ShopProfileForm();
        form.setShopName(merchant.getShopName());
        form.setShopDescription(merchant.getShopDescription());
        return form;
    }
}
