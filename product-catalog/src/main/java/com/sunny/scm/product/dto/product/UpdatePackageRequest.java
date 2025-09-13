package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UpdatePackageRequest {
    @JsonProperty("package_type")
    String packageType;

    @Digits(integer = 10, fraction = 2)
    BigDecimal length;

    @Digits(integer = 10, fraction = 2)
    BigDecimal width;

    @Digits(integer = 10, fraction = 2)
    BigDecimal height;

    @Digits(integer = 10, fraction = 2)
    BigDecimal weight;

    String barcode;

    @JsonProperty("quantity_in_parent")
    Integer quantityInParent;
}
