package com.sunny.scm.warehouse.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductPackageRequest {
    @JsonProperty("product_package_id")
    Long productPackageId;

    @JsonProperty("package_quantity")
    Integer packageQuantity;
}
