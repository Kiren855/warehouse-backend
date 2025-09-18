package com.sunny.scm.warehouse.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.constant.SourceType;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.dto.receipt.CreateGoodReceiptRequest;
import com.sunny.scm.warehouse.dto.receipt.ReceiptResponse;
import com.sunny.scm.warehouse.dto.zone.ZoneResponse;

import java.time.LocalDate;

public interface GoodReceiptService {
    ReceiptResponse getGoodReceipt(Long warehouseId, Long receiptId);
    void createGoodReceiptManual(Long warehouseId, CreateGoodReceiptRequest request);
    void processGoodReceipt(Long receiptId);
    void completeGoodReceipt(Long receiptId);
    void changeGoodReceiptStatus(Long warehouseId, Long receiptId, String status);
    void cancelGoodReceiptStatus(Long warehouseId, Long receiptId);

    PageResponse<ReceiptResponse> getReceipts(
            Long warehouseId,
            String keyword,
            SourceType sourceType,
            ReceiptStatus receiptStatus,
            int page,
            int size);
}
