package com.sunny.scm.warehouse.client;

import com.sunny.scm.grpc.product.GetProductPackagesRequest;
import com.sunny.scm.grpc.product.GetProductPackagesResponse;
import com.sunny.scm.grpc.product.ProductCatalogServiceGrpc;
import com.sunny.scm.grpc.product.ProductPackageRpc;
import com.sunny.scm.warehouse.dto.receipt.GroupedPackageDto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductCatalogClient {

    private final ProductCatalogServiceGrpc.ProductCatalogServiceBlockingStub productCatalogStub;

    public ProductCatalogClient(@GrpcClient("product-catalog-service") ProductCatalogServiceGrpc.ProductCatalogServiceBlockingStub productCatalogStub) {
        this.productCatalogStub = productCatalogStub;
    }
    public List<ProductPackageRpc> getProductPackages(List<GroupedPackageDto> packages) {
        GetProductPackagesRequest request = GetProductPackagesRequest.newBuilder()
                .addAllPackages(packages.stream()
                        .map(pkg -> com.sunny.scm.grpc.product.PackageQuantityRpc.newBuilder()
                                .setPackageId(pkg.getProductPackageId())
                                .setTotalQuantity(Math.toIntExact(pkg.getTotalQuantity()))
                                .build())
                        .toList())
                .build();

        GetProductPackagesResponse response = productCatalogStub.getProductPackages(request);

        return response.getProductPackagesList();
    }
}
