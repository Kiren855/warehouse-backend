package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.event.LoggingEvent;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.LogAction;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.dto.category.CreateCategoryRequest;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.event.LoggingProducer;
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
    private final LoggingProducer loggingProducer;
    @Override
    public void createCategory(CreateCategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = authentication.getName();
        String username = jwt.getClaimAsString("preferred_username");
        String companyId = jwt.getClaimAsString("company_id");

        Category newCategory = Category.builder()
                .companyId(Long.valueOf(companyId))
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .build();

        if(request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new AppException(ProductErrorCode.CATEGORY_NOT_EXIST));
            newCategory.setParentCategory(parent);
        }

        categoryRepository.save(newCategory);

        //logging
        String action = LogAction.CREATE_CATEGORY.format(request.getCategoryName());
        loggingProducer.sendMessage(userId, username, Long.valueOf(companyId), action);
    }
}
