package com.sunny.scm.warehouse.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.dto.receipt.GroupReceiptRequest;
import com.sunny.scm.warehouse.dto.receipt.GroupReceiptResponse;
import com.sunny.scm.warehouse.dto.receipt.ProductPackageDto;

import java.io.IOException;
import java.util.List;

public interface GroupReceiptService {
    void processGroupReceipts(Long warehouseId, GroupReceiptRequest request);

    PageResponse<GroupReceiptResponse> getGroupReceipts(Long warehouseId,
    String keyword, String status, int page, int size);

    void cancelGroupStatus(Long warehouseId, Long groupId);

    byte[] downloadPutawayList(Long groupReceiptId) throws IOException;
}
