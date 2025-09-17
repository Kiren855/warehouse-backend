package com.sunny.scm.product.grpc;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.grpc.product.*;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.repository.PackageRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class ProductCatalogServiceImpl extends ProductCatalogServiceGrpc.ProductCatalogServiceImplBase {

    private final PackageRepository packageRepository;

    @Override
    public void getProductPackage(GetProductPackagesRequest request,
                                  StreamObserver<GetProductPackagesResponse> responseObserver) {

        List<ProductPackage> productPackages = request.getPackageIdsList().stream()
                .map(packageId -> packageRepository.findById(packageId)
                        .orElseThrow(() -> new AppException(ProductErrorCode.PACKAGE_NOT_EXIST)))
                .map(pkg -> {
                    var productEntity = pkg.getProduct();

                    Product product = Product.newBuilder()
                            .setId(productEntity.getId())
                            .setCompanyId(productEntity.getCompanyId())
                            .setProductSku(productEntity.getProductSku())
                            .setProductName(productEntity.getProductName())
                            .setDescription(productEntity.getDescription() != null ? productEntity.getDescription() : "")
                            .setCategoryId(productEntity.getCategory() != null ? productEntity.getCategory().getId() : 0L)
                            .setCategoryName(productEntity.getCategory().getCategoryName())
                            .setUnit(productEntity.getUnit() != null ? productEntity.getUnit() : "")
                            .setStatus(productEntity.getStatus().name())
                            .build();

                    return ProductPackage.newBuilder()
                            .setId(pkg.getId())
                            .setProductId(productEntity.getId())
                            .setPackageType(pkg.getPackageType().name())
                            .setLength(pkg.getLength() != null ? pkg.getLength().doubleValue() : 0.0)
                            .setWidth(pkg.getWidth() != null ? pkg.getWidth().doubleValue() : 0.0)
                            .setHeight(pkg.getHeight() != null ? pkg.getHeight().doubleValue() : 0.0)
                            .setWeight(pkg.getWeight() != null ? pkg.getWeight().doubleValue() : 0.0)
                            .setBarcode(pkg.getBarcode() != null ? pkg.getBarcode() : "")
                            .setQuantityInParent(pkg.getQuantityInParent() != null ? pkg.getQuantityInParent() : 0)
                            .setProduct(product)
                            .build();
                })
                .collect(Collectors.toList());

        // Build response chứa list
        GetProductPackagesResponse response = GetProductPackagesResponse.newBuilder()
                .addAllProductPackages(productPackages)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
