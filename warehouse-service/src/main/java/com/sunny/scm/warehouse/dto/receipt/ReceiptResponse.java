package com.sunny.scm.warehouse.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReceiptResponse {
    @JsonProperty("receipt_number")
    String receiptNumber;

    @JsonProperty("source_type")
    String sourceType;

    @JsonProperty("receipt_status")
    String receiptStatus;


    @JsonProperty("create_at")
    LocalDateTime createdAt;
    @JsonProperty("update_at")
    LocalDateTime updatedAt;
}
