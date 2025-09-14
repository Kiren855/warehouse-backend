package com.sunny.scm.warehouse.dto.zone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateZoneRequest {
    @JsonProperty("zone_code")
    String zoneName;
    @JsonProperty("zone_name")
    String zoneType;
}
