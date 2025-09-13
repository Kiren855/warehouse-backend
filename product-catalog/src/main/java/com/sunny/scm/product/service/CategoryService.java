package com.sunny.scm.product.service;

import com.sunny.scm.product.dto.category.CategoryResponse;
import com.sunny.scm.product.dto.category.CreateCategoryRequest;
import com.sunny.scm.product.entity.Category;

import java.util.List;

public interface CategoryService {
    void createCategory(CreateCategoryRequest request);
    void deleteCategory(Long categoryId);

    List<String> getCategoryPath(Long leafCategoryId);
    public List<CategoryResponse> getRootCategories();

    public List<CategoryResponse> getChildCategories(Long parentId);
}
