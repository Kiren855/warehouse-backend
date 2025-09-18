package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.dto.bin.BinResponse;
import com.sunny.scm.warehouse.dto.bin.CreateBinRequest;
import com.sunny.scm.warehouse.dto.bin.UpdateBinRequest;
import com.sunny.scm.warehouse.entity.Bin;
import com.sunny.scm.warehouse.entity.Zone;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.helper.BinSpecifications;
import com.sunny.scm.warehouse.helper.EntityCodeGenerator;
import com.sunny.scm.warehouse.helper.ZoneSpecifications;
import com.sunny.scm.warehouse.repository.BinRepository;
import com.sunny.scm.warehouse.repository.ZoneRepository;
import com.sunny.scm.warehouse.service.BinService;
import com.sunny.scm.warehouse.service.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class BinServiceImpl implements BinService {
    private final BinRepository binRepository;
    private final ZoneRepository zoneRepository;
    private final SequenceService sequenceService;
    private final LoggingProducer loggingProducer;
    @Override
    @Transactional
    public void createBin(Long zoneId, CreateBinRequest request) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.ZONE_NOT_FOUND));

        Bin newBin = CreateBinRequest.toEntity(request);
        long sequence = sequenceService.getNextSequence("BIN", zone.getId());
        String binCode = EntityCodeGenerator.generateBinCode(zone.getZoneCode(), sequence);
        newBin.setBinCode(binCode);
        newBin.setZone(zone);

        binRepository.save(newBin);
        String action = LogAction.CREATE_BIN.format(binCode);
        loggingProducer.sendMessage(action);
    }

    @Override
    @CacheEvict(value = "bin_detail", key = "#zoneId + '_' + #BinId")
    public void updateBin(Long zoneId, Long BinId, UpdateBinRequest request) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.ZONE_NOT_FOUND));

        Bin existingBin = binRepository.findById(BinId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.BIN_NOT_FOUND));

        if (!existingBin.getZone().getId().equals(zone.getId())) {
            throw new AppException(WarehouseErrorCode.BIN_NOT_FOUND);
        }
        if(request.getBinName() != null) {
            existingBin.setBinName(request.getBinName());
        }
        if(request.getBinType() != null) {
            existingBin.setBinType(BinType.valueOf(request.getBinType()));
        }
        binRepository.save(existingBin);

        String action = LogAction.UPDATE_BIN.format(existingBin.getBinCode());
        loggingProducer.sendMessage(action);
    }

    @Override
    @Cacheable(value = "bin_detail", key = "#zoneId + '_' + #binId")
    public BinResponse getBin(Long zoneId, Long binId) {
        zoneRepository.findById(zoneId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.ZONE_NOT_FOUND));

        Bin bin = binRepository.findById(binId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.BIN_NOT_FOUND));

        if (!bin.getZone().getId().equals(zoneId)) {
            throw new AppException(WarehouseErrorCode.BIN_NOT_FOUND);
        }

        return BinResponse.toResponse(bin);
    }

    @Override
    public PageResponse<BinResponse> getBins(
    Long zoneId,
    String keyword,
    BinType binType,
    BinStatus binStatus,
    String contentStatus,
    LocalDate updatedFrom,
    LocalDate updatedTo,
    int page, int size, String sort)
    {
        zoneRepository.findById(zoneId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.ZONE_NOT_FOUND));

        String[] params = sort.split(",");
        Sort.Direction direction = params[1].equalsIgnoreCase("desc")
        ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, params[0]));

        Specification<Bin> spec = BinSpecifications.belongsToZone(zoneId)
                .and(BinSpecifications.likeCodeOrName(keyword))
                .and(BinSpecifications.hasBinType(binType))
                .and(BinSpecifications.hasStatus(binStatus))
                .and(BinSpecifications.hasContentStatus(contentStatus))
                .and(BinSpecifications.updatedBetween(updatedFrom, updatedTo));

        Page<Bin> bins = binRepository.findAll(spec, pageable);
        Page<BinResponse> binResponses = bins.map(BinResponse::toResponse);

        return PageResponse.from(binResponses);
    }
}
