package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.dto.zone.CreateZoneRequest;
import com.sunny.scm.warehouse.dto.zone.UpdateZoneRequest;
import com.sunny.scm.warehouse.dto.zone.ZoneResponse;
import com.sunny.scm.warehouse.entity.Warehouse;
import com.sunny.scm.warehouse.entity.Zone;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.helper.EntityCodeGenerator;
import com.sunny.scm.warehouse.helper.ZoneSpecifications;
import com.sunny.scm.warehouse.repository.WarehouseRepository;
import com.sunny.scm.warehouse.repository.ZoneRepository;
import com.sunny.scm.warehouse.service.SequenceService;
import com.sunny.scm.warehouse.service.ZoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {
    private final ZoneRepository zoneRepository;
    private final WarehouseRepository warehouseRepository;
    private final LoggingProducer loggingProducer;
    private final SequenceService sequenceService;

    @Override
    @Cacheable(value = "zone_detail", key = "#zoneId")
    public ZoneResponse getZone(Long warehouseId, Long zoneId) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.ZONE_NOT_FOUND));

        return ZoneResponse.builder()
                .id(zone.getId())
                .zoneCode(zone.getZoneCode())
                .zoneName(zone.getZoneName())
                .zoneType(zone.getZoneType().toString())
                .createdAt(zone.getCreationTimestamp())
                .updatedAt(zone.getUpdateTimestamp())
                .build();
    }

    @Override
    @Transactional
    public void createZone(Long warehouseId, CreateZoneRequest request) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        Zone newZone = CreateZoneRequest.toEntity(request);
        newZone.setWarehouse(warehouse);

        Long sequence = sequenceService.getNextSequence("ZONE", warehouse.getId());
        String zoneCode = EntityCodeGenerator.generateZoneCode(warehouse.getWarehouseCode(), sequence);
        newZone.setZoneCode(zoneCode);
        zoneRepository.save(newZone);

        String action = LogAction.CREATE_ZONE.format(zoneCode);
        loggingProducer.sendMessage(action);
    }

    @Override
    @CacheEvict(value = "zone_detail", key = "#zoneId")
    public void updateZone(Long warehouseId, Long zoneId, UpdateZoneRequest request) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.ZONE_NOT_FOUND));

        if(request.getZoneName() != null) {
           zone.setZoneName(request.getZoneName());
        }

        if(request.getZoneType() != null) {
            zone.setZoneType(ZoneType.valueOf(request.getZoneType().toUpperCase()));
        }

        zoneRepository.save(zone);
        String action = LogAction.UPDATE_ZONE.format(zone.getZoneCode());
        loggingProducer.sendMessage(action);
    }

    @Override
    @CacheEvict(value = "zone_detail", key = "#zoneId")
    public void deleteZone(Long warehouseId, Long zoneId) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.ZONE_NOT_FOUND));

        zoneRepository.delete(zone);
        String action = LogAction.DELETE_ZONE.format(zone.getZoneCode());
        loggingProducer.sendMessage(action);
    }

    @Override
    public PageResponse<ZoneResponse> getZones(
            Long warehouseId,
            String keyword,
            ZoneType zoneType,
            LocalDate updatedFrom,
            LocalDate updatedTo,
            int page, int size,
            String sort
    ) {
        // Sort
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        // Build Specification
        Specification<Zone> spec = ZoneSpecifications.belongsToWarehouse(warehouseId)
                .and(ZoneSpecifications.likeCodeOrName(keyword))
                .and(ZoneSpecifications.hasZoneType(zoneType))
                .and(ZoneSpecifications.updatedBetween(updatedFrom, updatedTo));

        Page<Zone> zones = zoneRepository.findAll(spec, pageable);

        Page<ZoneResponse> zoneResponses = zones.map(zone -> ZoneResponse.builder()
                .id(zone.getId())
                .zoneCode(zone.getZoneCode())
                .zoneName(zone.getZoneName())
                .zoneType(zone.getZoneType().toString())
                .createdAt(zone.getCreationTimestamp())
                .updatedAt(zone.getUpdateTimestamp())
                .build());

        return PageResponse.from(zoneResponses);
    }
}
