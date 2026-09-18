package com.sliit.se2030.mall.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryForm {

    @NotBlank(message = "Category name is required")
    private String name;

    private Long parentCategoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
