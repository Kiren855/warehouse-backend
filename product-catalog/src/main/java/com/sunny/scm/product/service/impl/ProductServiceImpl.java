package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.LogAction;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.constant.ProductStatus;
import com.sunny.scm.product.dto.product.ProductRequest;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.entity.Product;
import com.sunny.scm.product.event.LoggingProducer;
import com.sunny.scm.product.repository.CategoryRepository;
import com.sunny.scm.product.repository.ProductRepository;
import com.sunny.scm.product.service.ProductService;
import com.sunny.scm.product.service.SkuSequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final LoggingProducer loggingProducer;
    @Override
    public void createProduct(ProductRequest request) {
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
                .barcode(request.getBarcode())
                .build();

        productRepository.save(newProduct);
        String action = LogAction.CREATE_PRODUCT.format(newProduct.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
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
    public void deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));
        productRepository.delete(existingProduct);
        String action = LogAction.DELETE_PRODUCT.format(existingProduct.getProductSku());
        loggingProducer.sendMessage(action);
    }


}
