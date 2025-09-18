package com.sunny.scm.warehouse.dto.suggest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PutawaySuggestionDto {
    Long putawayGroupId;
    Long productPackageId;
    Long suggestedBinId;
    String suggestedBinName;
    String suggestedBinCode;
    Long suggestedZoneId;
    String suggestedZoneName;
    String suggestedZoneCode;
    Integer quantityToPutaway;
    String productSku;
    String productName;
    String packageBarcode;
}
