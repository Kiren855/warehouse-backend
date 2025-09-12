package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.product.entity.Product;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
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
public class ProductRequest {
    @JsonProperty("product_name")
    @NotBlank(message = "Product name is required")
    String productName;

    @JsonProperty("category_id")
    @NotBlank(message = "Category ID is required")
    Long categoryId;

    String description;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal length;

    @Digits(integer = 8, fraction = 2)
    private BigDecimal width;

    @Digits(integer = 8, fraction = 2)
    private BigDecimal height;

    @Digits(integer = 8, fraction = 2)
    private BigDecimal weight;

    @NotBlank
    String unit;

    String barcode;

    public static Product toEntity(ProductRequest request) {
        return Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .weight(request.getWeight())
                .unit(request.getUnit())
                .barcode(request.getBarcode())
                .build();
    }
}
