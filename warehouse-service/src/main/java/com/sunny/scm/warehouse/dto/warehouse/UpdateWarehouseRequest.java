package com.sunny.scm.warehouse.dto.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateWarehouseRequest {
    @JsonProperty("warehouse_name")
    String warehouseName;
    String location;
}
