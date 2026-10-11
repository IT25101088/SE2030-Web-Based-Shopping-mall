package com.sliit.se2030.mall.support.controller;

import com.sliit.se2030.mall.support.dto.InquiryResponseForm;
import com.sliit.se2030.mall.support.entity.InquiryStatus;
import com.sliit.se2030.mall.support.service.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("inquiries", inquiryService.listOpenInquiries());
        return "support/employee-inquiry-list";
    }

    @PostMapping("/{id}/respond")
    public String respond(@PathVariable Long id, @ModelAttribute InquiryResponseForm form) {
        inquiryService.respondToInquiry(id, form);
        return "redirect:/employee/inquiries";
    }

    @PostMapping("/{id}/resolve")
    public String resolve(@PathVariable Long id) {
        inquiryService.updateStatus(id, InquiryStatus.RESOLVED);
        return "redirect:/employee/inquiries";
    }
}
