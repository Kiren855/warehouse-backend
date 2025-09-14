package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.WarehouseResponse;
import com.sunny.scm.warehouse.entity.Warehouse;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.helper.EntityCodeGenerator;
import com.sunny.scm.warehouse.helper.WarehouseSpecifications;
import com.sunny.scm.warehouse.repository.WarehouseRepository;
import com.sunny.scm.warehouse.service.SequenceService;
import com.sunny.scm.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final LoggingProducer loggingProducer;
    private final SequenceService sequenceService;
    @Override
    @Transactional
    public void createWarehouse(CreateWarehouseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Warehouse newWarehouse = CreateWarehouseRequest.toEntity(request);
        newWarehouse.setCompanyId(Long.valueOf(companyId));

        Long sequence = sequenceService.getNextSequence("WAREHOUSE", Long.valueOf(companyId));
        String warehouseCode = EntityCodeGenerator.generateWarehouseCode(Long.valueOf(companyId), sequence);

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
    public void deleteWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        warehouseRepository.delete(warehouse);
        String action = LogAction.DELETE_WAREHOUSE.format(warehouse.getWarehouseCode());
        loggingProducer.sendMessage(action);
    }

    @Override
    public PageResponse<WarehouseResponse> getWarehouses(
            String keyword,
            LocalDate createdFrom,
            LocalDate createdTo,
            int page,
            int size,
            String sort
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long companyId = Long.valueOf(jwt.getClaimAsString("company_id"));

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Specification<Warehouse> spec = WarehouseSpecifications.belongsToCompany(companyId)
                .and(WarehouseSpecifications.likeCodeOrName(keyword))
                .and(WarehouseSpecifications.createdBetween(createdFrom, createdTo));

        Page<WarehouseResponse> warehouses = warehouseRepository.findAll(spec, pageable)
                .map(warehouse -> WarehouseResponse.builder()
                        .id(warehouse.getId())
                        .warehouseName(warehouse.getWarehouseName())
                        .warehouseCode(warehouse.getWarehouseCode())
                        .location(warehouse.getLocation())
                        .createdAt(warehouse.getCreationTimestamp())
                        .updatedAt(warehouse.getUpdateTimestamp())
                        .build());

        return PageResponse.from(warehouses);
    }

}
