package com.sunny.scm.product.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_clients.aop.CheckPermission;
import com.sunny.scm.product.constant.ProductSuccessCode;
import com.sunny.scm.product.dto.category.CreateCategoryRequest;
import com.sunny.scm.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/root")
    public ResponseEntity<?> getRootCategories() {
        var categories = categoryService.getRootCategories();
        ProductSuccessCode code = ProductSuccessCode.GET_ROOT_CATEGORIES_SUCCESS;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .result(categories)
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<?> getChildCategories(@PathVariable Long parentId) {
        var categories = categoryService.getChildCategories(parentId);
        ProductSuccessCode code = ProductSuccessCode.GET_CATEGORY_TREE_SUCCESS;

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .result(categories)
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }
}
