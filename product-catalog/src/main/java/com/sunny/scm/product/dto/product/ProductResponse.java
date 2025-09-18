package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    @JsonProperty("product_sku")
    String productSku;
    @JsonProperty("product_name")
    String productName;
    @JsonProperty("category_name")
    String categoryName;
    String unit;
    String status;
    @JsonProperty("product_type")
    String productType;
}
