package com.sunny.scm.warehouse.dto.bin;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.warehouse.entity.Bin;
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

    @JsonProperty("bin_status")
    String binStatus;

    @JsonProperty("create_at")
    LocalDateTime createdAt;
    @JsonProperty("update_at")
    LocalDateTime updatedAt;

    public static BinResponse toResponse(Bin bin) {
        BigDecimal maxVolume = bin.getLength()
                .multiply(bin.getWidth())
                .multiply(bin.getHeight());

        return BinResponse.builder()
                .id(bin.getId())
                .binCode(bin.getBinCode())
                .binName(bin.getBinName())
                .binType(bin.getBinType().toString())
                .binStatus(bin.getBinStatus().toString())
                .length(bin.getLength())
                .width(bin.getWidth())
                .height(bin.getHeight())
                .maxWeight(bin.getMaxWeight())
                .maxVolume(maxVolume)
                .currentWeight(bin.getCurrentWeightUsed())
                .currentVolume(bin.getCurrentVolumeUsed())
                .createdAt(bin.getCreationTimestamp())
                .updatedAt(bin.getUpdateTimestamp())
                .build();
    }
}
