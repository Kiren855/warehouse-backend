package com.sunny.scm.product.controller;

import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.product.constant.ProductSuccessCode;
import com.sunny.scm.product.dto.product.CreatePackageRequest;
import com.sunny.scm.product.dto.product.CreateProductRequest;
import com.sunny.scm.product.dto.product.UpdatePackageRequest;
import com.sunny.scm.product.dto.product.UpdateProductRequest;
import com.sunny.scm.product.service.PackageService;
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
    private final PackageService packageService;

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "VIEW_PRODUCT", "ALL_PERMISSIONS"})
    @GetMapping()
    public ResponseEntity<?> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        var products = productService.getProducts(page, size);

        ProductSuccessCode code = ProductSuccessCode.GET_PRODUCTS_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .result(products)
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "CREATE_PRODUCT", "ALL_PERMISSIONS"})
    @PostMapping()
    public ResponseEntity<?> createProduct(
        @Valid @RequestBody CreateProductRequest request) {
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
        @Valid @RequestBody UpdateProductRequest request) {
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

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "VIEW_PRODUCT", "ALL_PERMISSIONS"})
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long productId) {
        var productDetail = productService.getProductDetail(productId);
        ProductSuccessCode code = ProductSuccessCode.GET_PRODUCT_DETAIL_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .result(productDetail)
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "CREATE_PACKAGE", "ALL_PERMISSIONS"})
    @PostMapping("/{productId}/packages")
    public ResponseEntity<?> createPackage(
        @PathVariable Long productId,
        @Valid @RequestBody CreatePackageRequest request) {

        packageService.createPackage(productId, request);
        ProductSuccessCode code = ProductSuccessCode.CREATE_PACKAGE_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage()).build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "UPDATE_PACKAGE", "ALL_PERMISSIONS"})
    @PatchMapping("/{productId}/packages/{packageId}")
    public ResponseEntity<?> updatePackage(
            @PathVariable Long productId,
            @PathVariable Long packageId,
            @Valid @RequestBody UpdatePackageRequest request) {

        packageService.updatePackage(productId, packageId, request);
        ProductSuccessCode code = ProductSuccessCode.UPDATE_PACKAGE_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage()).build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "DELETE_PACKAGE", "ALL_PERMISSIONS"})
    @DeleteMapping("/{productId}/packages/{packageId}")
    public ResponseEntity<?> deletePackage(
    @PathVariable Long productId,
    @PathVariable Long packageId) {
        packageService.deletePackage(productId, packageId);
        ProductSuccessCode code = ProductSuccessCode.DELETE_PACKAGE_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage()).build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }

    @CheckPermission(permission = {"PRODUCT_CATALOG_MANAGER", "VIEW_PRODUCT", "ALL_PERMISSIONS"})
    @GetMapping("/{productId}/packages")
    public ResponseEntity<?> getPackages(
        @PathVariable Long productId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        var packages = packageService.getPackages(productId, page, size);
        ProductSuccessCode code = ProductSuccessCode.GET_PACKAGES_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .result(packages)
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(apiResponse);
    }
}
