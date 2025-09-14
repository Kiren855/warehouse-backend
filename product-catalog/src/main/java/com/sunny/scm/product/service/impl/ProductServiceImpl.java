package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.LogAction;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.dto.product.ProductDetailResponse;
import com.sunny.scm.product.dto.product.CreateProductRequest;
import com.sunny.scm.product.dto.product.ProductResponse;
import com.sunny.scm.product.dto.product.UpdateProductRequest;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    public void createProduct(CreateProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String StringCompanyId = jwt.getClaimAsString("company_id");
        long companyId = Long.parseLong(StringCompanyId);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ProductErrorCode.CATEGORY_NOT_EXIST));

        String sku = skuSequenceService.generateSku(companyId);

        Product newProduct = CreateProductRequest.toEntity(request);
        newProduct.setCompanyId(companyId);
        newProduct.setCategory(category);
        newProduct.setProductSku(sku);
        newProduct.setPackages(new HashSet<>());

        productRepository.save(newProduct);
        String action = LogAction.CREATE_PRODUCT.format(newProduct.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
    @CacheEvict(value = "product_details", key = "#productId")
    public void updateProduct(Long productId, UpdateProductRequest request) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));

        if (request.getProductName() != null) {
            existingProduct.setProductName(request.getProductName().trim());
        }

        if (request.getUnit() != null) {
            existingProduct.setUnit(request.getUnit().trim());
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

    @Override
    public PageResponse<ProductResponse> getProducts(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Page<ProductResponse> products = productRepository.findAllByCompanyId(
                Long.valueOf(companyId),
                PageRequest.of(page, size)
        ).map(product -> ProductResponse.builder()
                .id(product.getId())
                .productSku(product.getProductSku())
                .productName(product.getProductName())
                .categoryName(product.getCategory().getCategoryName())
                .unit(product.getUnit())
                .status(product.getStatus().name())
                .build());

        return PageResponse.from(products);
    }

    @Override
    public void changeStatus(Long productId, Boolean active) {

    }


}
