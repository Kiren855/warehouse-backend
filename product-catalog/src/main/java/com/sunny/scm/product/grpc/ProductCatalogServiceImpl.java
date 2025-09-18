package com.sunny.scm.product.grpc;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.grpc.product.*;
import com.sunny.scm.product.constant.ProductErrorCode;
import com.sunny.scm.product.entity.ProductPackage;
import com.sunny.scm.product.repository.PackageRepository;
import com.sunny.scm.product.repository.ProductRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ProductCatalogServiceImpl extends ProductCatalogServiceGrpc.ProductCatalogServiceImplBase {
    private final PackageRepository packageRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void getProductPackages(GetProductPackagesRequest request,
                                   StreamObserver<GetProductPackagesResponse> responseObserver) {
        try {
            List<PackageQuantityRpc> packages = request.getPackagesList();
            log.info("1. Received GetProductPackagesRequest with {} packages", packages.size());

            GetProductPackagesResponse.Builder responseBuilder = GetProductPackagesResponse.newBuilder();

            packages.forEach(pack -> {
                ProductPackage packageProduct = packageRepository.findById(pack.getPackageId())
                        .orElseThrow(() -> new AppException(ProductErrorCode.PACKAGE_NOT_EXIST));
                log.info("2. Fetched ProductPackage with ID: {}", packageProduct.getId());

                ProductPackageRpc packageRpc = ProductPackageRpc.newBuilder()
                        .setPackageId(packageProduct.getId())
                        .setProductId(packageProduct.getProduct().getId())
                        .setProductName(packageProduct.getProduct().getProductName())
                        .setProductSku(packageProduct.getProduct().getProductSku())
                        .setPackageType(packageProduct.getPackageType().name())
                        .setLength(packageProduct.getLength() != null ? packageProduct.getLength().toPlainString() : "0")
                        .setWidth(packageProduct.getWidth() != null ? packageProduct.getWidth().toPlainString() : "0")
                        .setHeight(packageProduct.getHeight() != null ? packageProduct.getHeight().toPlainString() : "0")
                        .setWeight(packageProduct.getWeight() != null ? packageProduct.getWeight().toPlainString() : "0")
                        .setBarcode(packageProduct.getBarcode())
                        .setQuantityInParent(packageProduct.getQuantityInParent())
                        .setTotalQuantity(pack.getTotalQuantity())
                        .build();

                responseBuilder.addProductPackages(packageRpc);
            });

            GetProductPackagesResponse response = responseBuilder.build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (AppException e) {
            Status status = Status.NOT_FOUND
                    .withDescription(e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            Status status = Status.INTERNAL
                    .withDescription("An unexpected error occurred: " + e.getMessage());
            responseObserver.onError(status.asRuntimeException());
        }
    }
}