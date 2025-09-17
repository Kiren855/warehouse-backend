package com.sunny.scm.warehouse.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.grpc.product.ProductPackageRpc;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPackageResponse {
    @JsonProperty("package_id")
    Long packageId;
    @JsonProperty("product_id")
    Long productId;
    @JsonProperty("product_sku")
    String productSku;
    @JsonProperty("product_name")
    String productName;
    @JsonProperty("package_type")
    String packageType;

    BigDecimal length;
    BigDecimal width;
    BigDecimal height;
    BigDecimal weight;
    String barcode;
    @JsonProperty("quantity_in_parent")
    Integer quantityInParent;

    public static ProductPackageResponse fromRpc(ProductPackageRpc rpc) {
        return ProductPackageResponse.builder()
                .packageId(rpc.getPackageId())
                .productId(rpc.getProductId())
                .productSku(rpc.getProductSku())
                .productName(rpc.getProductName())
                .packageType(rpc.getPackageType())
                .length(toBigDecimal(rpc.getLength()))
                .width(toBigDecimal(rpc.getWidth()))
                .height(toBigDecimal(rpc.getHeight()))
                .weight(toBigDecimal(rpc.getWeight()))
                .barcode(rpc.getBarcode())
                .quantityInParent(rpc.getQuantityInParent())
                .build();
    }

    private static BigDecimal toBigDecimal(String value) {
        return (value == null || value.isBlank()) ? null : new BigDecimal(value);
    }
}
