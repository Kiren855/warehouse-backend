package com.sunny.scm.warehouse.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class WarehouseResponse {
    Long id;
    @JsonProperty("warehouse_code")
    String warehouseCode;
    @JsonProperty("warehouse_name")
    String warehouseName;
    String location;

    Double latitude;
    Double longitude;

    @JsonProperty("create_at")
    LocalDateTime createdAt;
    @JsonProperty("update_at")
    LocalDateTime updatedAt;

    String status;
}
