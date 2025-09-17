package com.sunny.scm.warehouse.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupReceiptResponse {
    Long id;
    @JsonProperty("group_code")
    String groupCode;
    List<ReceiptResponse> receipts;

    @JsonProperty("total_receipts")
    Integer totalReceipts;
    @JsonProperty("created_at")
    LocalDateTime createdAt;
}
