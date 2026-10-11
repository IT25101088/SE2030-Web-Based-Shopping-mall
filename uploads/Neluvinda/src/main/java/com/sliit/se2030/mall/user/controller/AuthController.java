package com.sliit.se2030.mall.user.controller;

import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.user.dto.CustomerRegistrationForm;
import com.sliit.se2030.mall.user.dto.MerchantRegistrationForm;
import com.sliit.se2030.mall.user.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Note: there is no @PostMapping("/login") here. Spring Security's own
 * form-login filter (configured in SecurityConfig) intercepts POST /login
 * itself, before this controller ever sees the request -- we only need to
 * serve the login PAGE (GET), not handle the login submission.
 */
@Controller
public class AuthController {

    private final UserRegistrationService userRegistrationService;

    public AuthController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register/customer")
    public String customerRegistrationForm(Model model) {
        model.addAttribute("form", new CustomerRegistrationForm());
        return "auth/register-customer";
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@Valid @ModelAttribute("form") CustomerRegistrationForm form,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            return "auth/register-customer";
        }
        try {
            userRegistrationService.registerCustomer(form);
        } catch (BusinessRuleViolationException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/register-customer";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/register/merchant")
    public String merchantRegistrationForm(Model model) {
        model.addAttribute("form", new MerchantRegistrationForm());
        return "auth/register-merchant";
    }

    @PostMapping("/register/merchant")
    public String registerMerchant(@Valid @ModelAttribute("form") MerchantRegistrationForm form,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            return "auth/register-merchant";
        }
        try {
            userRegistrationService.registerMerchant(form);
        } catch (BusinessRuleViolationException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/register-merchant";
        }
        return "redirect:/login?registered";
    }
}
