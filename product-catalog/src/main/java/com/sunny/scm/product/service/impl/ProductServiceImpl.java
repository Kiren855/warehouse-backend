package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.LogAction;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.constant.ProductStatus;
import com.sunny.scm.product.dto.product.ProductDetailResponse;
import com.sunny.scm.product.dto.product.ProductRequest;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.entity.Product;
import com.sunny.scm.product.event.LoggingProducer;
import com.sunny.scm.product.repository.CategoryRepository;
import com.sunny.scm.product.repository.ProductRepository;
import com.sunny.scm.product.service.CategoryService;
import com.sunny.scm.product.service.ProductService;
import com.sunny.scm.product.service.SkuSequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SkuSequenceService skuSequenceService;
    private final LoggingProducer loggingProducer;
    private final CategoryService categoryService;
    @Override
    public void createProduct(ProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String StringCompanyId = jwt.getClaimAsString("company_id");
        long companyId = Long.parseLong(StringCompanyId);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ProductErrorCode.CATEGORY_NOT_EXIST));

        String sku = skuSequenceService.generateSku(companyId);

        Product newProduct = ProductRequest.toEntity(request);
        newProduct.setCategory(category);
        newProduct.setProductSku(sku);

        productRepository.save(newProduct);
        String action = LogAction.CREATE_PRODUCT.format(newProduct.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
    @CachePut(value = "product_details", key = "#productId")
    public void updateProduct(Long productId, ProductRequest request) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));

        if (request.getProductName() != null) {
            existingProduct.setProductName(request.getProductName().trim());
        }
        if (request.getLength() != null) {
            existingProduct.setLength(request.getLength());
        }
        if (request.getWidth() != null) {
            existingProduct.setWidth(request.getWidth());
        }
        if (request.getHeight() != null) {
            existingProduct.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            existingProduct.setWeight(request.getWeight());
        }
        if (request.getUnit() != null) {
            existingProduct.setUnit(request.getUnit().trim());
        }
        if (request.getBarcode() != null) {
            existingProduct.setBarcode(request.getBarcode().trim());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ProductErrorCode.CATEGORY_NOT_EXIST));
            existingProduct.setCategory(category);
        }

        productRepository.save(existingProduct);
        String action = LogAction.UPDATE_PRODUCT.format(existingProduct.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
    @CacheEvict(value = "product_details", key = "#productId")
    public void deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));
        productRepository.delete(existingProduct);
        String action = LogAction.DELETE_PRODUCT.format(existingProduct.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
    @Cacheable(value = "product_details", key = "#productId", unless = "#result == null")
    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));;

        List<String> categoryNames = categoryService.getCategoryPath(product.getCategory().getId());
        return ProductDetailResponse.fromEntity(product, categoryNames);
    }


}
