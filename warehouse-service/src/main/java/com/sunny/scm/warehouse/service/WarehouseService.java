package com.sunny.scm.warehouse.service;

import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;

public interface WarehouseService {
    void createWarehouse(CreateWarehouseRequest request);
    void updateWarehouse(Long warehouseId, UpdateWarehouseRequest request);
    void deleteWarehouse(Long warehouseId);
}
