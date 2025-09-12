package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.event.LoggingEvent;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.LogAction;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.dto.category.CreateCategoryRequest;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.event.LoggingProducer;
import com.sunny.scm.product.repository.CategoryRepository;
import com.sunny.scm.product.repository.ProductRepository;
import com.sunny.scm.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final LoggingProducer loggingProducer;
    @Override
    public void createCategory(CreateCategoryRequest request) {
        categoryRepository.findByCategoryName(request.getCategoryName())
                .ifPresent(c -> {
                    throw new AppException(ProductErrorCode.CATEGORY_ALREADY_EXISTS);
                });

        Category newCategory = Category.builder()
                .categoryName(request.getCategoryName())
                .build();

        if(request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new AppException(ProductErrorCode.CATEGORY_NOT_EXIST));
            newCategory.setParentCategory(parent);
        }

        categoryRepository.save(newCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ProductErrorCode.CATEGORY_NOT_EXIST));
        // check if category has products
        long count = productRepository.countByCategory(category);
        if(count > 0) {
            throw new AppException(ProductErrorCode.CATEGORY_HAS_PRODUCTS);
        }

        if (!category.getChildCategories().isEmpty()) {
            throw new AppException(ProductErrorCode.CATEGORY_HAS_CHILDREN);
        }

        categoryRepository.delete(category);
    }

    public List<String> getCategoryPath(Long leafCategoryId) {
        List<String> path = new ArrayList<>();

        Category currentCategory = categoryRepository.findById(leafCategoryId).orElse(null);

        while (currentCategory != null) {
            path.add(currentCategory.getCategoryName());
            currentCategory = currentCategory.getParentCategory();
        }

        Collections.reverse(path);
        return path;
    }
}
