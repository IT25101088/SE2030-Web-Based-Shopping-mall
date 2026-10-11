package com.sliit.se2030.mall.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Placeholder landing pages -- one per role, matching where
 * RoleBasedAuthenticationSuccessHandler sends people after login.
 * Other modules will add real content here later (order summaries,
 * product stats, etc.); for now these just prove the redirect works.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/customer/home")
    public String customerHome() {
        return "user/customer-home";
    }

    @GetMapping("/merchant/dashboard")
    public String merchantDashboard() {
        return "user/merchant-dashboard";
    }

    @GetMapping("/employee/dashboard")
    public String employeeDashboard() {
        return "user/employee-dashboard";
    }
}
