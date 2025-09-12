package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductDetailResponse {
    Long id;
    @JsonProperty("product_sku")
    String productSku;
    @JsonProperty("product_name")
    String productName;
    String description;
    @JsonProperty("category_names")
    List<String> categoryNames;
    String unit;
    String barcode;
    BigDecimal length;
    BigDecimal width;
    BigDecimal height;
    BigDecimal weight;
    String status;

    public static ProductDetailResponse fromEntity(Product product, List<String> categoryNames) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .productSku(product.getProductSku())
                .productName(product.getProductName())
                .description(product.getDescription())
                .length(product.getLength())
                .width(product.getWidth())
                .height(product.getHeight())
                .weight(product.getWeight())
                .unit(product.getUnit())
                .barcode(product.getBarcode())
                .productSku(product.getProductSku())
                .status(product.getStatus().name())
                .categoryNames(categoryNames)
                .build();
    }
}
