package com.sunny.scm.product.service;

import com.sunny.scm.product.dto.product.CreatePackageRequest;
import com.sunny.scm.product.dto.product.UpdatePackageRequest;

public interface PackageService {
    void createPackage(Long productId, CreatePackageRequest request);
    void updatePackage(Long productId, Long packageId, UpdatePackageRequest request);

    void deletePackage(Long productId, Long packageId);
}
