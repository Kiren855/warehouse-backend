package com.sunny.scm.grpc_clients.client;

import com.sunny.scm.grpc.product.GetProductPackagesRequest;
import com.sunny.scm.grpc.product.GetProductPackagesResponse;
import com.sunny.scm.grpc.product.ProductCatalogServiceGrpc;
import com.sunny.scm.grpc.product.ProductPackageRpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCatalogClient {

    private final ProductCatalogServiceGrpc.ProductCatalogServiceBlockingStub productCatalogStub;

    public ProductCatalogClient(@GrpcClient("product-catalog-service") ProductCatalogServiceGrpc.ProductCatalogServiceBlockingStub productCatalogStub) {
        this.productCatalogStub = productCatalogStub;
    }
    public List<ProductPackageRpc> getProductPackages(List<Long> packageIds) {
        GetProductPackagesRequest request = GetProductPackagesRequest.newBuilder()
                .addAllPackageIds(packageIds)
                .build();

        GetProductPackagesResponse response = productCatalogStub.getProductPackages(request);

        return response.getProductPackagesList();
    }
}
