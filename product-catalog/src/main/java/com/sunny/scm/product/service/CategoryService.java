package com.sunny.scm.product.service;

import com.sunny.scm.product.dto.category.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {
    void createCategory(CreateCategoryRequest request);
    void deleteCategory(Long categoryId);

    List<String> getCategoryPath(Long leafCategoryId);
}
