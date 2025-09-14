package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;
import com.sunny.scm.warehouse.entity.Warehouse;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.repository.WarehouseRepository;
import com.sunny.scm.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final LoggingProducer loggingProducer;
    @Override
    public void createWarehouse(CreateWarehouseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        long count = warehouseRepository.countByCompanyId(Long.valueOf(companyId));
        long sequence = count + 1;

        String warehouseCode = String.format("WH-%s-%03d", companyId, sequence);

        Warehouse newWarehouse = CreateWarehouseRequest.toEntity(request);
        newWarehouse.setCompanyId(Long.valueOf(companyId));
        newWarehouse.setWarehouseCode(warehouseCode);

        warehouseRepository.save(newWarehouse);
        String action = LogAction.CREATE_WAREHOUSE.format(warehouseCode);
        loggingProducer.sendMessage(action);
    }

    @Override
    public void updateWarehouse(Long warehouseId, UpdateWarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        if(request.getWarehouseName() != null) {
            warehouse.setWarehouseName(request.getWarehouseName());
        }

        if (request.getLocation() != null) {
            warehouse.setLocation(request.getLocation());
        }

        warehouseRepository.save(warehouse);
        String action = LogAction.UPDATE_WAREHOUSE.format(warehouse.getWarehouseCode());
        loggingProducer.sendMessage(action);
    }

    @Override
    public void deleteWarehouse() {

    }
}
