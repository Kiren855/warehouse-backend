package com.sunny.scm.warehouse.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.constant.SourceType;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.dto.receipt.CreateGoodReceiptRequest;
import com.sunny.scm.warehouse.dto.zone.ZoneResponse;

import java.time.LocalDate;

public interface GoodReceiptService {
    void createGoodReceiptManual(Long warehouseId, CreateGoodReceiptRequest request);
    void processGoodReceipt(Long receiptId);
    void completeGoodReceipt(Long receiptId);
    void changeGoodReceiptStatus(Long receiptId, String status);

    PageResponse<ZoneResponse> getReceipts(
            String keyword,
            SourceType sourceType,
            ReceiptStatus receiptStatus,
            int page,
            int size);
}
