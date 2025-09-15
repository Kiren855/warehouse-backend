package com.sunny.scm.warehouse.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateWarehouseRequest {
    @JsonProperty("warehouse_name")
    String warehouseName;
    String location;
    Double latitude;
    Double longitude;
}
