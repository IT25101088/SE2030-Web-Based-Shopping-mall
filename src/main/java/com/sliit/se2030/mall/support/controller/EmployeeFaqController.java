package com.sliit.se2030.mall.support.controller;

import com.sliit.se2030.mall.support.dto.FaqForm;
import com.sliit.se2030.mall.support.entity.FAQ;
import com.sliit.se2030.mall.support.service.FaqService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// Every URL under here is already restricted to ROLE_PLATFORM_EMPLOYEE by
// SecurityConfig's "/employee/**" rule -- nothing extra needed here for that.
@Controller
@RequestMapping("/employee/faqs")
public class EmployeeFaqController {

    private final FaqService faqService;

    public EmployeeFaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("faqs", faqService.listAll());
        return "support/employee-faq-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new FaqForm());
        return "support/employee-faq-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") FaqForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "support/employee-faq-form";
        }
        faqService.create(form);
        return "redirect:/employee/faqs";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        FAQ faq = faqService.getById(id);
        FaqForm form = new FaqForm();
        form.setQuestion(faq.getQuestion());
        form.setAnswer(faq.getAnswer());
        form.setCategory(faq.getCategory());
        model.addAttribute("form", form);
        model.addAttribute("faqId", id);
        return "support/employee-faq-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") FaqForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("faqId", id);
            return "support/employee-faq-form";
        }
        faqService.update(id, form);
        return "redirect:/employee/faqs";
    }

    @PostMapping("/{id}/publish")
    public String publish(@PathVariable Long id) {
        faqService.publish(id);
        return "redirect:/employee/faqs";
    }

    @PostMapping("/{id}/unpublish")
    public String unpublish(@PathVariable Long id) {
        faqService.unpublish(id);
        return "redirect:/employee/faqs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        faqService.delete(id);
        return "redirect:/employee/faqs";
    }
}
