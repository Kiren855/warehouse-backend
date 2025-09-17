package com.sunny.scm.product.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.product.dto.product.*;

import java.util.List;

public interface PackageService {
    void createPackage(Long productId, CreatePackageRequest request);
    void updatePackage(Long productId, Long packageId, UpdatePackageRequest request);

    void deletePackages(Long productId, DeletePackageRequest request);

    PageResponse<PackageResponse> getPackages(Long productId, int page, int size);

    List<PackageResponse> getAllPackages(Long productId);

    PageResponse<PackageDetailResponse> getPackagesByIds(List<Long> packageIds, int page, int size);
}
