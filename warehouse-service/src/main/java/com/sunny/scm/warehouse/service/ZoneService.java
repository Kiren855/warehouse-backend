package com.sunny.scm.warehouse.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.dto.warehouse.WarehouseResponse;
import com.sunny.scm.warehouse.dto.zone.CreateZoneRequest;
import com.sunny.scm.warehouse.dto.zone.UpdateZoneRequest;
import com.sunny.scm.warehouse.dto.zone.ZoneResponse;

import java.time.LocalDate;

public interface ZoneService {
    void createZone(Long warehouseId, CreateZoneRequest request);
    void updateZone(Long warehouseId, Long zoneId, UpdateZoneRequest request);

    void deleteZone(Long warehouseId, Long zoneId);

    PageResponse<ZoneResponse> getZones(
            Long warehouseId,
            String keyword,
            ZoneType zoneType,
            LocalDate createdFrom,
            LocalDate createdTo,
            int page,
            int size,
            String sort);
}
