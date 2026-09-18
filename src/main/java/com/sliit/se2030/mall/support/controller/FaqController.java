package com.sliit.se2030.mall.support.controller;

import com.sliit.se2030.mall.support.service.FaqService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// "/faq/**" is public (permitAll) in SecurityConfig -- only published FAQs are shown here.
@Controller
public class FaqController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @GetMapping("/faq")
    public String listFaqs(Model model) {
        model.addAttribute("faqs", faqService.listPublished());
        return "support/faq";
    }
}
