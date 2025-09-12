package com.sunny.scm.product.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.product.constant.ProductSuccessCode;
import com.sunny.scm.product.dto.category.CreateCategoryRequest;
import com.sunny.scm.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @CheckPermission(permission = {"ALL_PERMISSION","CREATE_CATEGORY"})
    @PostMapping()
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        categoryService.createCategory(request);
        ProductSuccessCode code = ProductSuccessCode.CREATE_CATEGORY_SUCCESS;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }
}
