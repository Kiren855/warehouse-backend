package com.sunny.scm.warehouse.dto.receipt;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChangeReceiptStatusRequest {
    @NotBlank(message = "Status is required")
    String status;
}
