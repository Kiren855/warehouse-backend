package com.sunny.scm.product.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.product.constant.ProductStatus;
import com.sunny.scm.product.dto.product.ProductDetailResponse;
import com.sunny.scm.product.dto.product.CreateProductRequest;
import com.sunny.scm.product.dto.product.ProductResponse;
import com.sunny.scm.product.dto.product.UpdateProductRequest;

public interface ProductService {
    void createProduct(CreateProductRequest request);
    void updateProduct(Long productId, UpdateProductRequest request);
    void deleteProduct(Long productId);

    ProductDetailResponse getProductDetail(Long productId);

    PageResponse<ProductResponse> getProducts(
            String keyword,
            int page, int size);

    PageResponse<ProductResponse> getProducts(int page, int size);
    void changeStatus(Long productId, Boolean active);
}
