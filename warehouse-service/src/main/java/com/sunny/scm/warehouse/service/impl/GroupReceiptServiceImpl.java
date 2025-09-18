package com.sunny.scm.warehouse.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.warehouse.client.AzureFileStorageClient;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.dto.receipt.*;
import com.sunny.scm.warehouse.dto.suggest.PutawaySuggestionDto;
import com.sunny.scm.warehouse.entity.GoodReceipt;
import com.sunny.scm.warehouse.entity.GroupReceipt;
import com.sunny.scm.warehouse.entity.Warehouse;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.helper.EntityCodeGenerator;
import com.sunny.scm.warehouse.helper.GroupReceiptSpecifications;
import com.sunny.scm.warehouse.repository.GoodReceiptItemRepository;
import com.sunny.scm.warehouse.repository.GroupReceiptRepository;
import com.sunny.scm.warehouse.repository.ReceiptRepository;
import com.sunny.scm.warehouse.repository.WarehouseRepository;
import com.sunny.scm.warehouse.service.GroupReceiptService;
import com.sunny.scm.warehouse.service.PutawayOptimizerService;
import com.sunny.scm.warehouse.service.PutawayPdfService;
import com.sunny.scm.warehouse.service.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupReceiptServiceImpl implements GroupReceiptService {
    private final GroupReceiptRepository groupReceiptRepository;
    private final WarehouseRepository warehouseRepository;
    private final ReceiptRepository receiptRepository;
    private final SequenceService sequenceService;
    private final LoggingProducer loggingProducer;
    private final PutawayOptimizerService putawayOptimizerService;
    private final PutawayPdfService putawayPdfService;
    private final AzureFileStorageClient azureFileStorageClient;
    private final BlobServiceClient blobServiceClient;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Value("${azure.blob.file-container-name}")
    private String fileContainerName;

    @Override
    @Transactional
    public void processGroupReceipts(Long warehouseId, GroupReceiptRequest request) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        Set<GoodReceipt> receipts = receiptRepository.findAllByIdIn(request.getReceiptIds());

        if (receipts.size() != request.getReceiptIds().size()) {
            throw new AppException(WarehouseErrorCode.RECEIPT_NOT_FOUND);
        }
        long sequence = sequenceService.getNextSequence("GROUP", warehouseId);
        String groupCode = EntityCodeGenerator.generateGroupCode(warehouse.getId(), sequence);
        GroupReceipt newGroupReceipt = GroupReceipt.builder()
                .warehouse(warehouse)
                .groupCode(groupCode)
                .receiptStatus(ReceiptStatus.CONFIRMED)
                .receipts(receipts)
                .build();

        receipts.forEach(r -> {
            r.setReceiptStatus(ReceiptStatus.CONFIRMED);
            r.setGroupReceipt(newGroupReceipt);
        });

        groupReceiptRepository.save(newGroupReceipt);
        PutawayOptimizer(warehouseId, newGroupReceipt.getId());
        String action = LogAction.CREATE_GROUP_RECEIPT.format(newGroupReceipt.getGroupCode());
        loggingProducer.sendMessage(action);
    }

    private void PutawayOptimizer(Long warehouseId, Long groupReceiptId) {

        List<PutawaySuggestionDto> suggestions = putawayOptimizerService.optimizePutawaySuggestions(warehouseId, groupReceiptId);
        try {
            byte[] pdfBytes = putawayPdfService.generatePutawayPdf(groupReceiptId, suggestions);
            InputStream pdfStream = new ByteArrayInputStream(pdfBytes);
            String fileUrl = azureFileStorageClient.uploadFile(fileContainerName,
                    "putaway_instructions_" + groupReceiptId + ".pdf", pdfStream, pdfBytes.length);

            GroupReceipt groupReceipt = groupReceiptRepository.findById(groupReceiptId)
                    .orElseThrow(() -> new AppException(WarehouseErrorCode.GROUP_RECEIPT_NOT_FOUND));
            groupReceipt.setPutawayListUrl(fileUrl);
            groupReceiptRepository.save(groupReceipt);

            String action = LogAction.GENERATE_PUTAWAY_PDF.format(groupReceipt.getGroupCode());
            loggingProducer.sendMessage(action);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<GroupReceiptResponse> getGroupReceipts(
    Long warehouseId,
    String keyword,
    String status,
     int page, int size) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        Specification<GroupReceipt> spec = GroupReceiptSpecifications.belongsToWarehouse(warehouseId)
                .and(GroupReceiptSpecifications.likeGroupCode(keyword))
                .and(GroupReceiptSpecifications.hasStatus(ReceiptStatus.valueOf(status)));

        Page<GroupReceipt> groupReceipts = groupReceiptRepository.findAll(spec, PageRequest.of(page, size));

        Page<GroupReceiptResponse> groupResponses = groupReceipts
                .map(groupReceipt -> GroupReceiptResponse.builder()
                        .id(groupReceipt.getId())
                        .groupCode(groupReceipt.getGroupCode())
                        .totalReceipts(groupReceipt.getReceipts().size())
                        .status(groupReceipt.getReceiptStatus().name())
                        .createdAt(groupReceipt.getCreationTimestamp())
                        .build());

        return PageResponse.from(groupResponses);
    }

    @Override
    public void cancelGroupStatus(Long warehouseId, Long groupId) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        GroupReceipt groupReceipt = groupReceiptRepository.findById(groupId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.GROUP_RECEIPT_NOT_FOUND));

        if (groupReceipt.getReceiptStatus() == ReceiptStatus.CANCELLED) {
            throw new AppException(WarehouseErrorCode.GROUP_RECEIPT_ALREADY_CANCELLED);
        }

        groupReceipt.setReceiptStatus(ReceiptStatus.CANCELLED);
        groupReceipt.getReceipts().forEach(r -> r.setReceiptStatus(ReceiptStatus.CANCELLED));
        groupReceiptRepository.save(groupReceipt);

        String action = LogAction.CANCEL_GROUP_RECEIPT.format(groupReceipt.getGroupCode());
        loggingProducer.sendMessage(action);
    }

    @Override
    public byte[] downloadPutawayList(Long groupReceiptId) throws IOException {
        GroupReceipt groupReceipt = groupReceiptRepository.findById(groupReceiptId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.GROUP_RECEIPT_NOT_FOUND));

        String fileUrl = groupReceipt.getPutawayListUrl();
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new AppException(WarehouseErrorCode.PUTAWAY_PDF_NOT_FOUND);
        }

        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(fileUrl)
                .buildClient();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            blobClient.downloadStream(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new AppException(GlobalErrorCode.FILE_CANNOT_DOWNLOAD);
        }
    }

}
