package com.sunny.scm.warehouse.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.WarehouseResponse;

import java.time.LocalDate;

public interface WarehouseService {
    void createWarehouse(CreateWarehouseRequest request);
    void updateWarehouse(Long warehouseId, UpdateWarehouseRequest request);
    void deleteWarehouse(Long warehouseId);
    PageResponse<WarehouseResponse> getWarehouses( String keyword,
                                                   LocalDate createdFrom,
                                                   LocalDate createdTo,
                                                   int page,
                                                   int size,
                                                   String sort);
}
