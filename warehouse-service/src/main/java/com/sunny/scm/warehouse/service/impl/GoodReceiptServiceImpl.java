package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.constant.SourceType;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.dto.receipt.CreateGoodReceiptRequest;
import com.sunny.scm.warehouse.dto.receipt.ReceiptItemResponse;
import com.sunny.scm.warehouse.dto.receipt.ReceiptResponse;
import com.sunny.scm.warehouse.entity.GoodReceipt;
import com.sunny.scm.warehouse.entity.GoodReceiptItem;
import com.sunny.scm.warehouse.entity.Warehouse;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.helper.EntityCodeGenerator;
import com.sunny.scm.warehouse.helper.ReceiptSpecifications;
import com.sunny.scm.warehouse.repository.ReceiptRepository;
import com.sunny.scm.warehouse.repository.WarehouseRepository;
import com.sunny.scm.warehouse.service.GoodReceiptService;
import com.sunny.scm.warehouse.service.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodReceiptServiceImpl implements GoodReceiptService {
    private final ReceiptRepository receiptRepository;
    private final WarehouseRepository warehouseRepository;
    private final SequenceService sequenceService;
    private final LoggingProducer loggingProducer;

    @Override
    public ReceiptResponse getGoodReceipt(Long warehouseId, Long receiptId) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        GoodReceipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.RECEIPT_NOT_FOUND));

        return ReceiptResponse.builder()
                .receiptId(receipt.getId())
                .receiptNumber(receipt.getReceiptNumber())
                .receiptStatus(receipt.getReceiptStatus().name())
                .totalItem(receipt.getItems().size())
                .sourceType(receipt.getSourceType().name())
                .createdAt(receipt.getCreationTimestamp())
                .updatedAt(receipt.getUpdateTimestamp())
                .items(receipt.getItems().stream()
                    .map(item -> ReceiptItemResponse.builder()
                            .productPackageId(item.getProductPackageId())
                            .packageQuantity(item.getPackageQuantity())
                            .build())
                .collect(Collectors.toList())).build();
    }

    @Override
    @Transactional
    public void createGoodReceiptManual(Long warehouseId, CreateGoodReceiptRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        long sequence = sequenceService.getNextSequence("GR", warehouse.getId());
        String receiptNumber = EntityCodeGenerator.generateReceiptNumber(Long.valueOf(companyId), sequence);

        GoodReceipt newReceipt = GoodReceipt.builder()
                .companyId(Long.valueOf(companyId))
                .warehouse(warehouse)
                .receiptNumber(receiptNumber)
                .receiptStatus(ReceiptStatus.PENDING)
                .sourceType(SourceType.PRODUCTION)
                .sourceId(null)
                .receiptDate(null)
                .items(new HashSet<>())
        .build();

        for (var item : request.getProductPackages()) {
            GoodReceiptItem receiptItem = GoodReceiptItem.builder()
                    .productPackageId(item.getProductPackageId())
                    .packageQuantity(item.getPackageQuantity())
                    .goodReceipt(newReceipt)
                    .build();
            newReceipt.getItems().add(receiptItem);
        }

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
    public void changeGoodReceiptStatus(Long warehouseId, Long receiptId, String status) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        GoodReceipt goodReceipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.RECEIPT_NOT_FOUND));

        goodReceipt.setReceiptStatus(ReceiptStatus.valueOf(status));
        receiptRepository.save(goodReceipt);
        String action = LogAction.CHANGE_RECEIPT_STATUS.format(goodReceipt.getReceiptNumber(), status);
        loggingProducer.sendMessage(action);
    }

    @Override
    public void cancelGoodReceiptStatus(Long warehouseId, Long receiptId) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        GoodReceipt goodReceipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.RECEIPT_NOT_FOUND));

        goodReceipt.setReceiptStatus(ReceiptStatus.CANCELLED);
        receiptRepository.save(goodReceipt);
        String action = LogAction.CANCEL_RECEIPT.format(goodReceipt.getReceiptNumber());
        loggingProducer.sendMessage(action);
    }

    @Override
    public PageResponse<ReceiptResponse> getReceipts(
    Long warehouseId,
    String keyword, SourceType sourceType,
    ReceiptStatus receiptStatus,
    int page, int size)
    {
        Specification<GoodReceipt> spec = ReceiptSpecifications.belongsToWarehouse(warehouseId)
                .and(ReceiptSpecifications.likeReceiptNumber(keyword))
                .and(ReceiptSpecifications.hasStatus(receiptStatus))
                .and(ReceiptSpecifications.hasSourceType(sourceType));

        Page<GoodReceipt> receiptsPage = receiptRepository.findAll(spec, PageRequest.of(page, size));

        Page<ReceiptResponse> receiptResponses = receiptsPage
                .map(receipts -> ReceiptResponse.builder()
                        .receiptId(receipts.getId())
                        .receiptNumber(receipts.getReceiptNumber())
                        .receiptStatus(receipts.getReceiptStatus().name())
                        .sourceType(receipts.getSourceType().name())
                        .totalItem(receipts.getItems().size())
                        .createdAt(receipts.getCreationTimestamp())
                        .updatedAt(receipts.getUpdateTimestamp()).build());
        return PageResponse.from(receiptResponses);

    }
}
