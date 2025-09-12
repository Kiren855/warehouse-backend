package com.sunny.scm.product.service;

import com.sunny.scm.product.dto.product.ProductRequest;

public interface ProductService {
    void createProduct(ProductRequest request);
    void updateProduct(Long productId, ProductRequest request);
    void deleteProduct(Long productId);
}
