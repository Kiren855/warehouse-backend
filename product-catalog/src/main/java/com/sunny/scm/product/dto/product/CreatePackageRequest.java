package com.sunny.scm.product.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.product.constant.PackageType;
import com.sunny.scm.product.entity.ProductPackage;
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
public class CreatePackageRequest {
    @NotBlank(message = "Package type must not be blank")
    @JsonProperty("package_type")
    String packageType;

    @Digits(integer = 10, fraction = 2)
    @NotBlank(message = "Length must not be blank")
    private BigDecimal length;

    @Digits(integer = 10, fraction = 2)
    @NotBlank(message = "Width must not be blank")
    private BigDecimal width;

    @Digits(integer = 10, fraction = 2)
    @NotBlank(message = "Height must not be blank")
    private BigDecimal height;

    @Digits(integer = 10, fraction = 2)
    @NotBlank(message = "Weight must not be blank")
    private BigDecimal weight;

    @NotBlank(message = "Barcode must not be blank")
    String barcode;

    @JsonProperty("quantity_in_parent")
    @NotBlank(message = "Quantity in parent must not be blank")
    Integer quantityInParent;

    public static ProductPackage toEntity(CreatePackageRequest request) {
        return ProductPackage.builder()
                .packageType(Enum.valueOf(PackageType.class, request.getPackageType()))
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .weight(request.getWeight())
                .barcode(request.getBarcode())
                .quantityInParent(request.getQuantityInParent())
                .build();
    }
}
