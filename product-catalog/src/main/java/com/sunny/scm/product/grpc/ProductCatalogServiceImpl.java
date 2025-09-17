package com.sunny.scm.product.grpc;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.grpc.product.*;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.entity.ProductPackage;
import com.sunny.scm.product.repository.PackageRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class ProductCatalogServiceImpl extends ProductCatalogServiceGrpc.ProductCatalogServiceImplBase {

    private final PackageRepository packageRepository;

    @Override
    public void getProductPackages(GetProductPackagesRequest request,
                                   StreamObserver<GetProductPackagesResponse> responseObserver) {

        List<Long> packageIds = request.getPackageIdsList();

        List<ProductPackage> packageEntities = packageRepository.findAllById(packageIds);

        Set<Long> foundIds = packageEntities.stream().map(ProductPackage::getId).collect(Collectors.toSet());
        List<Long> missingIds = packageIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();
        if (!missingIds.isEmpty()) {
            throw new AppException(ProductErrorCode.PACKAGE_NOT_EXIST);
        }

        List<com.sunny.scm.grpc.product.ProductPackage> productPackages = packageEntities.stream()
                .map(pkg -> {
                    var productEntity = pkg.getProduct();

                    Product product = Product.newBuilder()
                            .setId(productEntity.getId())
                            .setCompanyId(productEntity.getCompanyId())
                            .setProductSku(productEntity.getProductSku())
                            .setProductName(productEntity.getProductName())
                            .setDescription(productEntity.getDescription() != null ? productEntity.getDescription() : "")
                            .setCategoryId(productEntity.getCategory() != null ? productEntity.getCategory().getId() : 0L)
                            .setCategoryName(productEntity.getCategory() != null ? productEntity.getCategory().getCategoryName() : "")
                            .setUnit(productEntity.getUnit() != null ? productEntity.getUnit() : "")
                            .setStatus(productEntity.getStatus() != null ? productEntity.getStatus().name() : "")
                            .build();

                    return com.sunny.scm.grpc.product.ProductPackage.newBuilder()
                            .setId(pkg.getId())
                            .setProductId(productEntity.getId())
                            .setPackageType(pkg.getPackageType() != null ? pkg.getPackageType().name() : "")
                            .setLength(pkg.getLength() != null ? pkg.getLength().doubleValue() : 0.0)
                            .setWidth(pkg.getWidth() != null ? pkg.getWidth().doubleValue() : 0.0)
                            .setHeight(pkg.getHeight() != null ? pkg.getHeight().doubleValue() : 0.0)
                            .setWeight(pkg.getWeight() != null ? pkg.getWeight().doubleValue() : 0.0)
                            .setBarcode(pkg.getBarcode() != null ? pkg.getBarcode() : "")
                            .setQuantityInParent(pkg.getQuantityInParent() != null ? pkg.getQuantityInParent() : 0)
                            .setProduct(product)
                            .build();
                })
                .toList();

        // Build response
        GetProductPackagesResponse response = GetProductPackagesResponse.newBuilder()
                .addAllProductPackages(productPackages)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductPackage(GetProductPackagesRequest request, StreamObserver<GetProductPackagesResponse> responseObserver) {

    }
}
