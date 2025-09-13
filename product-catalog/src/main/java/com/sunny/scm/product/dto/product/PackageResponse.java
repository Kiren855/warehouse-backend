package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PackageResponse {
    Long id;
    @JsonProperty("package_name")
    String packageName;
    @JsonProperty("package_type")
    String packageType;

    BigDecimal length;
    BigDecimal width;
    BigDecimal height;
    BigDecimal weight;

    String barcode;
    @JsonProperty("quantity_in_parent")
    Integer quantityInParent;
}
