package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
    @JsonProperty("product_name")
    String productName;
    @JsonProperty("category_id")
    Long categoryId;
    String description;
    BigDecimal length;
    BigDecimal width;
    BigDecimal height;
    BigDecimal weight;
    @JsonProperty("barcode")
    String barcode;
    String unit;

}
