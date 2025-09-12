package com.sunny.scm.product.service;

import com.sunny.scm.product.dto.product.ProductDetailResponse;
import com.sunny.scm.product.dto.product.CreateProductRequest;
import com.sunny.scm.product.dto.product.UpdateProductRequest;

public interface ProductService {
    void createProduct(CreateProductRequest request);
    void updateProduct(Long productId, UpdateProductRequest request);
    void deleteProduct(Long productId);

    ProductDetailResponse getProductDetail(Long productId);
}
