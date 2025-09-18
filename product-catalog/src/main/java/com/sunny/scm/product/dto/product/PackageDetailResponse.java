package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageDetailResponse {
    @JsonProperty("package_id")
    Long packageId;

    @JsonProperty("product_name")
    String productName;

    @JsonProperty("product_sku")
    String productSku;

    @JsonProperty("package_type")
    String packageType;

    String barcode;
    BigDecimal weight;
    BigDecimal length;
    BigDecimal width;
    BigDecimal height;
}
