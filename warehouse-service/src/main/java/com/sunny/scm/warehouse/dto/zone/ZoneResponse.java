package com.sunny.scm.warehouse.dto.zone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ZoneResponse {
    Long id;
    @JsonProperty("zone_code")
    String zoneCode;
    @JsonProperty("zone_name")
    String zoneName;
    @JsonProperty("zone_type")
    String zoneType;
    @JsonProperty("create_at")
    LocalDateTime createdAt;
    @JsonProperty("update_at")
    LocalDateTime updatedAt;
}
