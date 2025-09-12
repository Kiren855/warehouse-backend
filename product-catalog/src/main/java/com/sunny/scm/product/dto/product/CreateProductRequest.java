package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateProductRequest {
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
}
