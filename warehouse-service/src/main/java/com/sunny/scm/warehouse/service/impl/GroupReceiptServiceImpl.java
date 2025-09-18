package com.sunny.scm.warehouse.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.grpc.product.ProductPackageRpc;
import com.sunny.scm.warehouse.client.AzureFileStorageClient;
import com.sunny.scm.warehouse.client.ProductCatalogClient;
import com.sunny.scm.warehouse.constant.LogAction;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.constant.TransactionType;
import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.dto.receipt.*;
import com.sunny.scm.warehouse.dto.suggest.PutawaySuggestionDto;
import com.sunny.scm.warehouse.entity.*;
import com.sunny.scm.warehouse.event.LoggingProducer;
import com.sunny.scm.warehouse.helper.EntityCodeGenerator;
import com.sunny.scm.warehouse.helper.GroupReceiptSpecifications;
import com.sunny.scm.warehouse.repository.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final PutawayReservationRepository putawayReservationRepository;
    private final ProductCatalogClient productCatalogClient;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final BinRepository binRepository;
    private final BinContentRepository binContentRepository;

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
    @Transactional
    public void cancelGroupStatus(Long warehouseId, Long groupId) {
        warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        GroupReceipt groupReceipt = groupReceiptRepository.findById(groupId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.GROUP_RECEIPT_NOT_FOUND));

        if (groupReceipt.getReceiptStatus() == ReceiptStatus.CANCELLED) {
            throw new AppException(WarehouseErrorCode.GROUP_RECEIPT_ALREADY_CANCELLED);
        }

        putawayReservationRepository.deleteByGroupReceiptId(groupReceipt.getId());
        groupReceipt.setReceiptStatus(ReceiptStatus.CANCELLED);
        groupReceipt.getReceipts().forEach(r -> r.setReceiptStatus(ReceiptStatus.CANCELLED));
        groupReceiptRepository.save(groupReceipt);

        String action = LogAction.CANCEL_GROUP_RECEIPT.format(groupReceipt.getGroupCode());
        loggingProducer.sendMessage(action);
    }

    @Transactional
    @Override
    public void completeGroupStatus(Long warehouseId, Long groupId) {
        GroupReceipt groupReceipt = groupReceiptRepository.findById(groupId)
                .orElseThrow(() -> new AppException(WarehouseErrorCode.GROUP_RECEIPT_NOT_FOUND));

        List<PutawayReservation> reservations = putawayReservationRepository.findByGroupReceiptId(groupId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AppException(GlobalErrorCode.UNAUTHENTICATED);
        }
        String userId = authentication.getName();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        long companyId = Long.parseLong(jwt.getClaimAsString("company_id"));

        // Concurrent structures
        Map<String, AtomicInteger> binContentMap = new ConcurrentHashMap<>();
        List<InventoryTransaction> transactions = Collections.synchronizedList(new ArrayList<>());

        // Async processing
        List<CompletableFuture<Void>> futures = reservations.stream()
                .map(reservation -> CompletableFuture.runAsync(() ->
                                processSingleReservation(reservation, binContentMap, transactions, userId, companyId),
                        virtualThreadExecutor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        binContentMap.forEach((key, qty) -> {
            String[] parts = key.split("-");
            Long binId = Long.parseLong(parts[0]);
            Long packageId = Long.parseLong(parts[1]);

            Bin bin = binRepository.findById(binId)
                    .orElseThrow(() -> new AppException(WarehouseErrorCode.BIN_NOT_FOUND));

            BinContent binContent = binContentRepository
                    .findByBinIdAndProductPackageId(binId, packageId)
                    .orElse(BinContent.builder()
                            .bin(bin)
                            .productPackageId(packageId)
                            .quantity(0)
                            .build());

            binContent.setQuantity(binContent.getQuantity() + qty.get());
            binContentRepository.save(binContent);
        });

        inventoryTransactionRepository.saveAll(transactions);

        putawayReservationRepository.deleteByGroupReceiptId(groupId);

        groupReceipt.setReceiptStatus(ReceiptStatus.COMPLETED);
        groupReceipt.getReceipts().forEach(r -> r.setReceiptStatus(ReceiptStatus.COMPLETED));
        groupReceiptRepository.save(groupReceipt);

        loggingProducer.sendMessage(LogAction.COMPLETE_GROUP_RECEIPT.format(groupReceipt.getGroupCode()));
    }

    private void processSingleReservation(PutawayReservation reservation,
                                          Map<String, AtomicInteger> binContentMap,
                                          List<InventoryTransaction> transactions,
                                          String userId,
                                          long companyId) {

        Bin bin = binRepository.findById(reservation.getBin().getId())
                .orElseThrow(() -> new AppException(WarehouseErrorCode.BIN_NOT_FOUND));

        GroupedPackageDto groupedPackageDto = GroupedPackageDto.builder()
                .productPackageId(reservation.getProductPackageId())
                .totalQuantity((long) reservation.getQuantityReserved())
                .build();
        ProductPackageRpc productPackage = productCatalogClient
                .getProductPackages(Collections.singletonList(groupedPackageDto)).get(0);

        // Cập nhật bin
        synchronized (bin) {
            BigDecimal addVolume = new BigDecimal(productPackage.getLength())
                    .multiply(new BigDecimal(productPackage.getWidth()))
                    .multiply(new BigDecimal(productPackage.getHeight()))
                    .multiply(BigDecimal.valueOf(reservation.getQuantityReserved()));

            BigDecimal addWeight = new BigDecimal(productPackage.getWeight())
                    .multiply(BigDecimal.valueOf(reservation.getQuantityReserved()));

            bin.setCurrentVolumeUsed(bin.getCurrentVolumeUsed().add(addVolume));
            bin.setCurrentWeightUsed(bin.getCurrentWeightUsed().add(addWeight));
            binRepository.save(bin);
        }

        // Gom BinContent
        String key = bin.getId() + "-" + reservation.getProductPackageId();
        binContentMap.computeIfAbsent(key, k -> new AtomicInteger(0))
                .addAndGet(reservation.getQuantityReserved());

        // Tạo transaction
        InventoryTransaction tx = InventoryTransaction.builder()
                .companyId(companyId)
                .packageProductId(productPackage.getPackageId())
                .fromBin(null)
                .toBin(bin)
                .quantity(BigDecimal.valueOf(reservation.getQuantityReserved()))
                .transactionType(TransactionType.INBOUND)
                .referenceDocId(reservation.getGroupReceipt().getId())
                .referenceDocType("GROUP_RECEIPT")
                .transactionDate(LocalDateTime.now())
                .userId(userId)
                .note("Putaway completed")
                .build();
        transactions.add(tx);
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
