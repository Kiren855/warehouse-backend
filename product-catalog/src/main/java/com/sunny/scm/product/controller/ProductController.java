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
    @CheckPermission(permission = {"CREATE_PRODUCT", ""})
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


}
