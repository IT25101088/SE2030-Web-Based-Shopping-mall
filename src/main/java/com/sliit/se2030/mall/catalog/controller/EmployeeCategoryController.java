package com.sliit.se2030.mall.catalog.controller;

import com.sliit.se2030.mall.catalog.dto.CategoryForm;
import com.sliit.se2030.mall.catalog.entity.Category;
import com.sliit.se2030.mall.catalog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Every URL under here is already restricted to ROLE_PLATFORM_EMPLOYEE by
// SecurityConfig's "/employee/**" rule -- nothing extra needed here for that.
@Controller
@RequestMapping("/employee/categories")
public class EmployeeCategoryController {

    private final CategoryService categoryService;

    public EmployeeCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.listAll());
        return "catalog/employee-category-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new CategoryForm());
        model.addAttribute("categories", categoryService.listAll());
        return "catalog/employee-category-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") CategoryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.listAll());
            return "catalog/employee-category-form";
        }
        categoryService.create(form);
        return "redirect:/employee/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id);
        CategoryForm form = new CategoryForm();
        form.setName(category.getName());
        if (category.getParentCategory() != null) {
            form.setParentCategoryId(category.getParentCategory().getId());
        }
        model.addAttribute("form", form);
        model.addAttribute("categoryId", id);
        model.addAttribute("categories", categoryService.listAll());
        return "catalog/employee-category-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") CategoryForm form,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryId", id);
            model.addAttribute("categories", categoryService.listAll());
            return "catalog/employee-category-form";
        }
        categoryService.update(id, form);
        return "redirect:/employee/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/employee/categories";
    }
}
