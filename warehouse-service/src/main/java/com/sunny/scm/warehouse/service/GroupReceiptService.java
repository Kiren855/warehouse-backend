package com.sunny.scm.warehouse.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.dto.receipt.GroupReceiptRequest;
import com.sunny.scm.warehouse.dto.receipt.GroupReceiptResponse;

public interface GroupReceiptService {
    void processGroupReceipts(Long warehouseId, GroupReceiptRequest request);

    PageResponse<GroupReceiptResponse> getGroupReceipts(Long warehouseId,
    String keyword, String status, int page, int size);

}
