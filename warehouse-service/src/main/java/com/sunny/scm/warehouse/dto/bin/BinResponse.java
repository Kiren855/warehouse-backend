package com.sunny.scm.warehouse.dto.bin;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BinResponse {
    Long id;
    @JsonProperty("bin_code")
    String binCode;
    @JsonProperty("bin_name")
    String binName;
    @JsonProperty("bin_type")
    String binType;

    BigDecimal length;
    BigDecimal width;
    BigDecimal height;

    @JsonProperty("max_weight")
    BigDecimal maxWeight;
    @JsonProperty("max_volume")
    BigDecimal maxVolume;

    @JsonProperty("current_weight")
    BigDecimal currentWeight;
    @JsonProperty("current_volume")
    BigDecimal currentVolume;

    String status;

    @JsonProperty("create_at")
    LocalDateTime createdAt;
    @JsonProperty("update_at")
    LocalDateTime updatedAt;
}
