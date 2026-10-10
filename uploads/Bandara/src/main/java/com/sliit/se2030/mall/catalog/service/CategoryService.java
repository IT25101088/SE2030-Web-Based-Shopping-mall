package com.sliit.se2030.mall.catalog.service;

import com.sliit.se2030.mall.catalog.entity.Category;
import com.sliit.se2030.mall.catalog.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }
}
