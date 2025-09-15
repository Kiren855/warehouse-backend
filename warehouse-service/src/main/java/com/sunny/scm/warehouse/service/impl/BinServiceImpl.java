package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.dto.bin.BinResponse;
import com.sunny.scm.warehouse.dto.bin.CreateBinRequest;
import com.sunny.scm.warehouse.dto.bin.UpdateBinRequest;
import com.sunny.scm.warehouse.service.BinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BinServiceImpl implements BinService {
    @Override
    public void createBin(Long zoneId, CreateBinRequest request) {

    }

    @Override
    public void updateBin(Long zoneId, Long BinId, UpdateBinRequest request) {

    }

    @Override
    public BinResponse getBin(Long zoneId, Long binId) {
        return null;
    }

    @Override
    public PageResponse<BinResponse> getBins(Long zoneId, String keyword, BinType binType, BinStatus binStatus, int page, int size, String sort) {
        return null;
    }
}
