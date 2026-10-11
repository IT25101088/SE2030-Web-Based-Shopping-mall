package com.sliit.se2030.mall.support.controller;

import com.sliit.se2030.mall.support.dto.InquiryResponseForm;
import com.sliit.se2030.mall.support.entity.Inquiry;
import com.sliit.se2030.mall.support.service.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// "/employee/**" already restricted to ROLE_PLATFORM_EMPLOYEE by SecurityConfig.
@Controller
@RequestMapping("/employee/inquiries")
public class EmployeeInquiryController {

    private final InquiryService inquiryService;

    public EmployeeInquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping
    public String listOpenInquiries(Model model) {
        List<Inquiry> inquiries = inquiryService.listOpenInquiries();
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("suggestedShops", inquiryService.suggestShops(inquiries));
        model.addAttribute("merchants", inquiryService.listApprovedMerchants());
        return "support/employee-inquiry-list";
    }

    @PostMapping("/{id}/respond")
    public String respond(@PathVariable Long id, @ModelAttribute InquiryResponseForm form) {
        inquiryService.respondAsEmployee(id, form);
        return "redirect:/employee/inquiries";
    }

    @PostMapping("/{id}/forward")
    public String forward(@PathVariable Long id, @RequestParam Long merchantId) {
        inquiryService.forwardToMerchant(id, merchantId);
        return "redirect:/employee/inquiries";
    }

    @PostMapping("/{id}/take-back")
    public String takeBack(@PathVariable Long id) {
        inquiryService.takeBackFromMerchant(id);
        return "redirect:/employee/inquiries";
    }

    @PostMapping("/{id}/resolve")
    public String resolve(@PathVariable Long id) {
        inquiryService.resolve(id);
        return "redirect:/employee/inquiries";
    }
}
