package com.sunny.scm.warehouse.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.entity.GoodReceipt;
import com.sunny.scm.warehouse.entity.GoodReceiptItem;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateGoodReceiptRequest {
    @JsonProperty("product_packages")
    List<ProductPackageRequest> productPackages;

    public static GoodReceipt toEntity(CreateGoodReceiptRequest request) {
        return GoodReceipt.builder()
                .items(request.getProductPackages().stream()
                        .map(pp -> GoodReceiptItem.builder()
                                .productPackageId(pp.getProductPackageId())
                                .packageQuantity(pp.getPackageQuantity())
                                .build())
                        .collect(Collectors.toSet()))
                .receiptStatus(ReceiptStatus.PENDING)
                .build();
    }
}
