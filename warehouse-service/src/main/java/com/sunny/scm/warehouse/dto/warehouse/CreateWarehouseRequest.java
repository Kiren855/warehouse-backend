package com.sunny.scm.warehouse.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.warehouse.constant.WarehouseStatus;
import com.sunny.scm.warehouse.entity.Warehouse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateWarehouseRequest {

    @JsonProperty("warehouse_code")
    @NotBlank(message = "warehouseCode is required")
    @Size(max = 10, message = "warehouseCode must not exceed 10 characters")
    String warehouseCode;

    @NotBlank(message = "warehouseName is required")
    @JsonProperty("warehouse_name")
    String warehouseName;

    @NotBlank(message = "location is required")
    String location;

    Double latitude;
    Double longitude;

    public static Warehouse toEntity(CreateWarehouseRequest request) {
        return Warehouse.builder()
                .warehouseName(request.getWarehouseName())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .status(WarehouseStatus.ACTIVE)
                .build();
    }
}
