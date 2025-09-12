package com.sunny.scm.product.controller;

import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.product.constant.ProductSuccessCode;
import com.sunny.scm.product.dto.product.ProductRequest;
import com.sunny.scm.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "CREATE_PRODUCT", "ALL_PERMISSIONS"})
    @PostMapping()
    public ResponseEntity<ApiResponse<?>> createProduct(
        @Valid @RequestBody ProductRequest request) {
        productService.createProduct(request);
        ProductSuccessCode code = ProductSuccessCode.CREATE_PRODUCT_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage()).build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "UPDATE_PRODUCT", "ALL_PERMISSIONS"})
    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody ProductRequest request) {
        productService.updateProduct(productId, request);
        ProductSuccessCode code = ProductSuccessCode.UPDATE_PRODUCT_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage()).build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "DELETE_PRODUCT", "ALL_PERMISSIONS"})
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        ProductSuccessCode code = ProductSuccessCode.DELETE_PRODUCT_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage()).build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }


}
