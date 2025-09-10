package com.sunny.scm.product.controller;

import com.sunny.scm.common.aop.CheckPermission;
import com.sunny.scm.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    @CheckPermission(permission = "ALL_PERMI")
    @GetMapping()
    public ResponseEntity<?> testRole() {

        return ResponseEntity.ok(ApiResponse.builder()
                .code("100000")
                .message("can use this api")
                .build());
    }
}
