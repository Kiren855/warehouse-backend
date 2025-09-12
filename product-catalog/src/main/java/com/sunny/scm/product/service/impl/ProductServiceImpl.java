package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.constant.ProductStatus;
import com.sunny.scm.product.dto.product.CreateProductRequest;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.entity.Product;
import com.sunny.scm.product.repository.CategoryRepository;
import com.sunny.scm.product.repository.ProductRepository;
import com.sunny.scm.product.repository.SkuSequenceRepository;
import com.sunny.scm.product.service.ProductService;
import com.sunny.scm.product.service.SkuSequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SkuSequenceService skuSequenceService;
    @Override
    public void createProduct(CreateProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String StringCompanyId = jwt.getClaimAsString("company_id");
        long companyId = Long.parseLong(StringCompanyId);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ProductErrorCode.CATEGORY_NOT_EXIST));

        String sku = skuSequenceService.generateSku(companyId);

        Product newProduct = Product.builder()
                .productName(request.getProductName())
                .productSku(sku)
                .companyId(companyId)
                .category(category)
                .width(request.getWidth())
                .length(request.getLength())
                .height(request.getHeight())
                .weight(request.getWeight())
                .unit(request.getUnit())
                .status(ProductStatus.INACTIVE)
                .build();
    }
}
