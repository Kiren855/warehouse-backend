package com.sunny.scm.warehouse.service;

import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;

public interface WarehouseService {
    void createWarehouse(CreateWarehouseRequest request);
    void updateWarehouse();
    void deleteWarehouse();
}
