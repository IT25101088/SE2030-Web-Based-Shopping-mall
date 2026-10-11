package com.sliit.se2030.mall.user.controller;

import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.user.dto.CustomerProfileForm;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.service.CustomerProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// "/customer/**" is already restricted to ROLE_CUSTOMER by SecurityConfig.
@Controller
@RequestMapping("/customer/profile")
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    public CustomerProfileController(CustomerProfileService customerProfileService) {
        this.customerProfileService = customerProfileService;
    }

    @GetMapping
    public String viewProfile(Model model) {
        Customer customer = customerProfileService.getOwnProfile();
        model.addAttribute("form", toForm(customer));
        model.addAttribute("email", customer.getEmail());
        return "user/customer-profile";
    }

    @PostMapping
    public String updateProfile(@Valid @ModelAttribute("form") CustomerProfileForm form,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            model.addAttribute("email", customerProfileService.getOwnProfile().getEmail());
            return "user/customer-profile";
        }
        customerProfileService.updateProfile(form);
        return "redirect:/customer/profile?updated";
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam(value = "currentPassword", required = false) String currentPassword,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                RedirectAttributes redirectAttributes) {
        try {
            customerProfileService.deleteOwnAccount(currentPassword);
        } catch (BusinessRuleViolationException ex) {
            redirectAttributes.addFlashAttribute("deleteError", ex.getMessage());
            return "redirect:/customer/profile";
        }
        // The account no longer exists as far as login is concerned, so end
        // this session the same way the normal Log out button does.
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login?deleted";
    }

    private CustomerProfileForm toForm(Customer customer) {
        CustomerProfileForm form = new CustomerProfileForm();
        form.setFullName(customer.getFullName());
        form.setPhone(customer.getPhone());
        form.setShippingAddress(customer.getShippingAddress());
        return form;
    }
}
