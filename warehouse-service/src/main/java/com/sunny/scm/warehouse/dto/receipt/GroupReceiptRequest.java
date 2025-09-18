package com.sunny.scm.warehouse.dto.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GroupReceiptRequest {
    @JsonProperty("receipt_ids")
    List<Long> receiptIds;
}
