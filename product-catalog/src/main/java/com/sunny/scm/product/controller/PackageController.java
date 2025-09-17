package com.sunny.scm.product.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.product.constant.ProductSuccessCode;
import com.sunny.scm.product.dto.product.PackageIdsRequest;
import com.sunny.scm.product.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/api/v1/packages")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_RECEIPT", "ALL_PERMISSIONS"})
    @PostMapping
    public ResponseEntity<?> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody PackageIdsRequest request
            ) {
        var packages = packageService.getPackagesByIds(request.getPackageIds(), page, size);
        ProductSuccessCode code = ProductSuccessCode.GET_PACKAGES_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus()).body(ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .result(packages)
                .build());

    }

}
