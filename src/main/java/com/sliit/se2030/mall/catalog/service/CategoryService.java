package com.sliit.se2030.mall.catalog.service;

import com.sliit.se2030.mall.catalog.dto.CategoryForm;
import com.sliit.se2030.mall.catalog.entity.Category;
import com.sliit.se2030.mall.catalog.repository.CategoryRepository;
import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
    }

    public Category create(CategoryForm form) {
        if (categoryRepository.findByName(form.getName()).isPresent()) {
            throw new BusinessRuleViolationException("A category named '" + form.getName() + "' already exists.");
        }
        Category category = new Category(form.getName());
        category.setParentCategory(resolveParent(form.getParentCategoryId(), null));
        return categoryRepository.save(category);
    }

    @Transactional
    public void update(Long categoryId, CategoryForm form) {
        Category category = getById(categoryId);
        categoryRepository.findByName(form.getName())
                .filter(existing -> !existing.getId().equals(categoryId))
                .ifPresent(existing -> {
                    throw new BusinessRuleViolationException("A category named '" + form.getName() + "' already exists.");
                });
        category.setName(form.getName());
        category.setParentCategory(resolveParent(form.getParentCategoryId(), categoryId));
        // dirty checking, no explicit save()
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = getById(categoryId);
        if (!productRepository.findByCategory_Id(categoryId).isEmpty()) {
            throw new BusinessRuleViolationException(
                    "This category still has products assigned to it and cannot be removed.");
        }
        categoryRepository.delete(category);
    }

    private Category resolveParent(Long parentCategoryId, Long selfId) {
        if (parentCategoryId == null) {
            return null;
        }
        if (parentCategoryId.equals(selfId)) {
            throw new BusinessRuleViolationException("A category cannot be its own parent.");
        }
        return getById(parentCategoryId);
    }
}
