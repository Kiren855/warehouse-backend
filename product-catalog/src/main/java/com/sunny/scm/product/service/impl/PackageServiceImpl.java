package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.LogAction;
import com.sunny.scm.product.constant.PackageType;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.dto.product.*;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.entity.Product;
import com.sunny.scm.product.entity.ProductPackage;
import com.sunny.scm.product.event.LoggingProducer;
import com.sunny.scm.product.repository.PackageRepository;
import com.sunny.scm.product.repository.ProductRepository;
import com.sunny.scm.product.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepository;
    private final ProductRepository productRepository;
    private final LoggingProducer loggingProducer;
    @Override
    @CacheEvict(value = "product_details", key = "#productId")
    public void createPackage(Long productId, CreatePackageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));

        ProductPackage productPackage = CreatePackageRequest.toEntity(request);
        productPackage.setProduct(product);
        packageRepository.save(productPackage);
        String action = LogAction.ADD_PRODUCT_PACKAGE.format(productPackage.getPackageType(), product.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
    @CacheEvict(value = "product_details", key = "#productId")
    public void updatePackage(Long productId, Long packageId, UpdatePackageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));

        ProductPackage productPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PACKAGE_NOT_EXIST));

        if(request.getPackageType() != null) {
            PackageType type = PackageType.valueOf(request.getPackageType().toUpperCase());
            productPackage.setPackageType(type);
        }

        if (request.getLength() != null) {
            productPackage.setLength(request.getLength());
        }

        if (request.getWidth() != null) {
            productPackage.setWidth(request.getWidth());
        }

        if (request.getHeight() != null) {
            productPackage.setHeight(request.getHeight());
        }

        if (request.getWeight() != null) {
            productPackage.setWeight(request.getWeight());
        }

        if (request.getBarcode() != null) {
            productPackage.setBarcode(request.getBarcode());
        }

        if (request.getQuantityInParent() != null) {
            productPackage.setQuantityInParent(request.getQuantityInParent());
        }

        packageRepository.save(productPackage);
        String action = LogAction.UPDATE_PRODUCT_PACKAGE.format(productPackage.getPackageType(), product.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
    @CacheEvict(value = "product_details", key = "#productId")
    public void deletePackages(Long productId, DeletePackageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));

        if(request.getPackageIds().isEmpty()) {
           return;
        }
        Set<ProductPackage> packages = request.getPackageIds().stream()
                .map(packageId -> packageRepository.findById(packageId)
                        .orElseThrow(() -> new AppException(ProductErrorCode.PACKAGE_NOT_EXIST)))
                .collect(Collectors.toSet());

        product.getPackages().removeAll(packages);
        productRepository.save(product);

        String action = LogAction.REMOVE_PRODUCT_PACKAGE.format(packages.size(), product.getProductSku());
        loggingProducer.sendMessage(action);
    }

    @Override
    public PageResponse<PackageResponse> getPackages(Long productId, int page, int size) {
        Page<PackageResponse> packages = packageRepository
            .findAllByProductId(productId, PageRequest.of(page, size))
                .map(productPackage -> PackageResponse.builder()
                        .id(productPackage.getId())
                        .packageType(productPackage.getPackageType().name())
                        .width(productPackage.getWidth())
                        .length(productPackage.getLength())
                        .height(productPackage.getHeight())
                        .weight(productPackage.getWeight())
                        .barcode(productPackage.getBarcode())
                        .quantityInParent(productPackage.getQuantityInParent())
                        .build());

        return PageResponse.from(packages);
    }

    @Override
    public List<PackageResponse> getAllPackages(Long productId) {
        return packageRepository
                .findAllByProductId(productId)
                .stream()
                .map(productPackage -> PackageResponse.builder()
                        .id(productPackage.getId())
                        .packageType(productPackage.getPackageType().name())
                        .width(productPackage.getWidth())
                        .length(productPackage.getLength())
                        .height(productPackage.getHeight())
                        .weight(productPackage.getWeight())
                        .barcode(productPackage.getBarcode())
                        .quantityInParent(productPackage.getQuantityInParent())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<PackageDetailResponse> getPackagesByIds(List<Long> packageIds, int page, int size) {
        Page<PackageDetailResponse> packages = packageRepository
                .findAllByIdIn(packageIds, PageRequest.of(page, size))
                .map(productPackage -> PackageDetailResponse.builder()
                        .packageId(productPackage.getId())
                        .packageType(productPackage.getPackageType().name())
                        .width(productPackage.getWidth())
                        .length(productPackage.getLength())
                        .height(productPackage.getHeight())
                        .weight(productPackage.getWeight())
                        .barcode(productPackage.getBarcode())
                        .productName(productPackage.getProduct().getProductName())
                        .productSku(productPackage.getProduct().getProductSku())
                        .build());

        return PageResponse.from(packages);
    }
}
