package com.sliit.se2030.mall.support.controller;

import com.sliit.se2030.mall.support.dto.InquiryForm;
import com.sliit.se2030.mall.support.service.InquiryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// STUB -- "/inquiries/**" already restricted to ROLE_CUSTOMER by SecurityConfig.
@Controller
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping
    public String myInquiries(Model model) {
        model.addAttribute("inquiries", inquiryService.getInquiriesForCurrentCustomer());
        return "support/my-inquiries";
    }

    @GetMapping("/new")
    public String newInquiryForm(Model model) {
        model.addAttribute("form", new InquiryForm());
        return "support/submit-inquiry";
    }

    @PostMapping
    public String submitInquiry(@Valid @ModelAttribute("form") InquiryForm form, BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", bindingResult.getFieldErrors());
            return "support/submit-inquiry";
        }
        inquiryService.submitInquiry(form);
        return "redirect:/inquiries";
    }
}
