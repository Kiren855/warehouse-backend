package com.sunny.scm.product.service.impl;

import com.sunny.scm.product.repository.CategoryRepository;
import com.sunny.scm.product.repository.ProductRepository;
import com.sunny.scm.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    @Override
    public void createProduct(com.sunny.scm.product.dto.product.CreateProductRequest request) {

    }
}
