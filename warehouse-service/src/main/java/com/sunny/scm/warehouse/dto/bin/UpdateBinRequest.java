package com.sunny.scm.warehouse.dto.bin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateBinRequest {
    @JsonProperty("bin_name")
    String binName;

    @JsonProperty("bin_type")
    String binType;
    @JsonProperty("bin_status")
    String binStatus;
    String length;
    String width;
    String height;

    @JsonProperty("max_weight")
    String maxWeight;
}
