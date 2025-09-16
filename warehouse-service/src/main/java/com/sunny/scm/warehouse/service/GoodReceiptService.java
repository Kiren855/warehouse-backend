package com.sunny.scm.warehouse.service;

import com.sunny.scm.warehouse.dto.receipt.CreateGoodReceiptRequest;

public interface GoodReceiptService {
    void createGoodReceiptManual(Long warehouseId, CreateGoodReceiptRequest request);
    void processGoodReceipt(Long receiptId);
    void completeGoodReceipt(Long receiptId);
    void changeGoodReceiptStatus(Long receiptId, String status);
}
