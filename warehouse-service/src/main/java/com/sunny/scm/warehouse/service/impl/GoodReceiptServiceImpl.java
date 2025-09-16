package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.SourceType;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.dto.receipt.CreateGoodReceiptRequest;
import com.sunny.scm.warehouse.entity.GoodReceipt;
import com.sunny.scm.warehouse.entity.Warehouse;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.helper.EntityCodeGenerator;
import com.sunny.scm.warehouse.repository.CodeSequenceRepository;
import com.sunny.scm.warehouse.repository.ReceiptRepository;
import com.sunny.scm.warehouse.repository.WarehouseRepository;
import com.sunny.scm.warehouse.service.GoodReceiptService;
import com.sunny.scm.warehouse.service.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodReceiptServiceImpl implements GoodReceiptService {
    private final ReceiptRepository receiptRepository;
    private final WarehouseRepository warehouseRepository;
    private final SequenceService sequenceService;
    private final LoggingProducer loggingProducer;
    @Override
    public void createGoodReceiptManual(Long warehouseId, CreateGoodReceiptRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        long sequence = sequenceService.getNextSequence("GR", warehouse.getId());
        String receiptNumber = EntityCodeGenerator.generateReceiptNumber(Long.valueOf(companyId), sequence);

        GoodReceipt newReceipt = CreateGoodReceiptRequest.toEntity(request);
        newReceipt.setWarehouse(warehouse);
        newReceipt.setCompanyId(Long.valueOf(companyId));
        newReceipt.setReceiptNumber(receiptNumber);
        newReceipt.setSourceType(SourceType.PRODUCTION);

        receiptRepository.save(newReceipt);
        String action = LogAction.CREATE_RECEIPT.format(receiptNumber);
        loggingProducer.sendMessage(action);
    }

    @Override
    public void processGoodReceipt(Long receiptId) {

    }

    @Override
    public void completeGoodReceipt(Long receiptId) {

    }

    @Override
    public void changeGoodReceiptStatus(Long receiptId, String status) {

    }
}
