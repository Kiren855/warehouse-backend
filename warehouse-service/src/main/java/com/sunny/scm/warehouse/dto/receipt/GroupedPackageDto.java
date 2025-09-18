package com.sunny.scm.warehouse.dto.receipt;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupedPackageDto {
    Long productPackageId;
    LocalDate expirationDate;
    Long totalQuantity;
}
