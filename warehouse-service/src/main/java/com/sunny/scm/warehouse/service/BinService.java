package com.sunny.scm.warehouse.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.dto.bin.BinResponse;
import com.sunny.scm.warehouse.dto.bin.CreateBinRequest;
import com.sunny.scm.warehouse.dto.bin.UpdateBinRequest;

public interface BinService {
    void createBin(Long zoneId, CreateBinRequest request);
    void updateBin(Long zoneId, Long BinId, UpdateBinRequest request);

    BinResponse getBin(Long zoneId, Long binId);
    PageResponse<BinResponse> getBins(
            Long zoneId,
            String keyword,
            BinType binType,
            BinStatus binStatus,
            int page,
            int size,
            String sort);

}
