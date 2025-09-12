package com.sunny.scm.product.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.product.constant.LogAction;
import com.sunny.scm.product.constant.PackageType;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.dto.product.CreatePackageRequest;
import com.sunny.scm.product.dto.product.UpdatePackageRequest;
import com.sunny.scm.product.entity.Product;
import com.sunny.scm.product.entity.ProductPackage;
import com.sunny.scm.product.event.LoggingProducer;
import com.sunny.scm.product.repository.PackageRepository;
import com.sunny.scm.product.repository.ProductRepository;
import com.sunny.scm.product.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {
    private final PackageRepository packageRepository;
    private final ProductRepository productRepository;
    private final LoggingProducer loggingProducer;
    @Override
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
    public void deletePackage(Long productId, Long packageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_EXIST));

        ProductPackage productPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PACKAGE_NOT_EXIST));

        packageRepository.delete(productPackage);
        String action = LogAction.REMOVE_PRODUCT_PACKAGE.format(productPackage.getPackageType(), product.getProductSku());
        loggingProducer.sendMessage(action);
    }
}
