package com.sunny.scm.warehouse.dto.zone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.entity.Zone;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateZoneRequest {
    @JsonProperty("zone_name")
    @NotBlank(message = "Zone name is required")
    String zoneName;
    @JsonProperty("zone_type")
    @NotBlank(message = "Zone type is required")
    String zoneType;

    public static Zone toEntity(CreateZoneRequest request) {
        return Zone.builder()
                .zoneName(request.getZoneName())
                .zoneType(ZoneType.valueOf(request.getZoneType()))
                .build();
    }
}
