package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.product.constant.ProductStatus;
import com.sunny.scm.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class CreateProductRequest {
    @JsonProperty("product_name")
    @NotBlank(message = "Product name must not be blank")
    String productName;

    @JsonProperty("category_id")
    @NotBlank(message = "Category ID must not be blank")
    Long categoryId;

    String description;

    @NotBlank(message = "Unit must not be blank")
    String unit;

    public static Product toEntity(CreateProductRequest request) {
        return Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .status(ProductStatus.INACTIVE)
                .build();
    }
}
