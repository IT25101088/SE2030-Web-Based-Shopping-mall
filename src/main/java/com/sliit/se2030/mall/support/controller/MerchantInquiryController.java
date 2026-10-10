package com.sliit.se2030.mall.support.controller;

import com.sliit.se2030.mall.support.dto.InquiryResponseForm;
import com.sliit.se2030.mall.support.service.InquiryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// "/merchant/**" already restricted to ROLE_MERCHANT by SecurityConfig.
// There is deliberately no resolve endpoint here -- the shop answers, then the
// mall team reviews that answer and resolves.
@Controller
@RequestMapping("/merchant/inquiries")
public class MerchantInquiryController {

    private final InquiryService inquiryService;

    public MerchantInquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping
    public String myShopInquiries(Model model) {
        model.addAttribute("inquiries", inquiryService.getInquiriesForCurrentMerchant());
        return "support/merchant-inquiry-list";
    }

    @PostMapping("/{id}/respond")
    public String respond(@PathVariable Long id, @ModelAttribute InquiryResponseForm form) {
        inquiryService.respondAsMerchant(id, form);
        return "redirect:/merchant/inquiries";
    }
}
