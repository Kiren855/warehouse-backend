package com.sunny.scm.product.service.impl;

import com.sunny.scm.product.dto.category.CreateCategoryRequest;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.repository.CategoryRepository;
import com.sunny.scm.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public void createCategory(CreateCategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Category newCategory = Category.builder()
                .companyId(Long.valueOf(companyId))
                .categoryName(request.getCategoryName())
                .categoryName(request.getDescription())
                .build();

        if(request.getParentId() != null) {
            categoryRepository.findById(request.getParentId()).orElseThrow(() -> n);
        }
        categoryRepository.save();
    }
}
