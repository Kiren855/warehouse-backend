package com.sunny.scm.warehouse.dto.bin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.entity.Bin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateBinRequest {
    @JsonProperty("bin_name")
    String binName;
    @JsonProperty("bin_type")
    String binType;

    BigDecimal length;
    BigDecimal width;
    BigDecimal height;

    @JsonProperty("max_weight")
    BigDecimal maxWeight;

    public static Bin toEntity(CreateBinRequest request) {
        return Bin.builder()
                .binName(request.getBinName())
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .maxWeight(request.getMaxWeight())
                .binType(BinType.valueOf(request.getBinType()))
                .binStatus(BinStatus.ACTIVE)
                .binContents(new HashSet<>())
                .currentVolumeUsed(BigDecimal.ZERO)
                .currentWeightUsed(BigDecimal.ZERO)
                .build();
    }
}
