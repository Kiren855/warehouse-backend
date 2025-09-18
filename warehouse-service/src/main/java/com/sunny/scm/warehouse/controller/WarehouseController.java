package com.sunny.scm.warehouse.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_clients.aop.CheckPermission;
import com.sunny.scm.warehouse.constant.*;
import com.sunny.scm.warehouse.dto.receipt.ChangeReceiptStatusRequest;
import com.sunny.scm.warehouse.dto.receipt.CreateGoodReceiptRequest;
import com.sunny.scm.warehouse.dto.receipt.GroupReceiptRequest;
import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;
import com.sunny.scm.warehouse.service.GoodReceiptService;
import com.sunny.scm.warehouse.service.GroupReceiptService;
import com.sunny.scm.warehouse.service.PutawayOptimizerService;
import com.sunny.scm.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/warehouse/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final GoodReceiptService goodReceiptService;
    private final GroupReceiptService groupReceiptService;
    private final PutawayOptimizerService putawayOptimizerService;

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_WAREHOUSE", "ALL_PERMISSIONS"})
    @GetMapping("/{warehouseId}")
    public ResponseEntity<?> getWarehouse(@PathVariable Long warehouseId) {
        var response = warehouseService.getWarehouse(warehouseId);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_WAREHOUSE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_WAREHOUSE", "ALL_PERMISSIONS"})
    @GetMapping()
    public ResponseEntity<?> getWarehouses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationTimestamp,desc") String sort)
    {
        WarehouseStatus warehouseStatus = null;
        if (status != null && !status.isBlank()) {
            warehouseStatus = WarehouseStatus.valueOf(status.toUpperCase());
        }

        var response = warehouseService.getWarehouses(keyword, warehouseStatus, createdFrom, createdTo, page, size, sort);

        WarehouseSuccessCode code = WarehouseSuccessCode.GET_WAREHOUSES_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "CREATE_WAREHOUSE", "ALL_PERMISSIONS"})
    @PostMapping()
    public ResponseEntity<?> createWarehouse(
    @Valid @RequestBody CreateWarehouseRequest request)
    {
        warehouseService.createWarehouse(request);
        WarehouseSuccessCode code = WarehouseSuccessCode.CREATE_WAREHOUSE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "UPDATE_WAREHOUSE", "ALL_PERMISSIONS"})
    @PatchMapping("/{warehouseId}")
    public ResponseEntity<?> updateWarehouse(
            @PathVariable Long warehouseId,
            @Valid @RequestBody UpdateWarehouseRequest request)
    {
        warehouseService.updateWarehouse(warehouseId, request);
        WarehouseSuccessCode code = WarehouseSuccessCode.UPDATE_WAREHOUSE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "DELETE_WAREHOUSE", "ALL_PERMISSIONS"})
    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
        WarehouseSuccessCode code = WarehouseSuccessCode.DELETE_WAREHOUSE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "CREATE_RECEIPT", "ALL_PERMISSIONS"})
    @PostMapping("/{warehouseId}/good-receipts")
    public ResponseEntity<?> createGoodReceiptManual(
            @PathVariable Long warehouseId,
            @Valid @RequestBody CreateGoodReceiptRequest request)
    {
        goodReceiptService.createGoodReceiptManual(warehouseId, request);
        WarehouseSuccessCode code = WarehouseSuccessCode.CREATE_GOOD_RECEIPT_SUCCESS;

        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_RECEIPT", "ALL_PERMISSIONS"})
    @GetMapping("/{warehouseId}/good-receipts")
    public ResponseEntity<?> getGoodReceipts(
            @PathVariable Long warehouseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sourceType,
            @RequestParam(required = false) String receiptStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {
        SourceType type = null;
        if (sourceType != null && !sourceType.isBlank()) {
            type = SourceType.valueOf(sourceType.toUpperCase());
        }

        ReceiptStatus status = null;
        if (receiptStatus != null && !receiptStatus.isBlank()) {
            status = ReceiptStatus.valueOf(receiptStatus.toUpperCase());
        }

        var response = goodReceiptService.getReceipts(warehouseId, keyword, type, status, page, size);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_GOOD_RECEIPTS_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_RECEIPT", "ALL_PERMISSIONS"})
    @GetMapping("/{warehouseId}/good-receipts/{receiptId}")
    public ResponseEntity<?> getReceiptDetail(
            @PathVariable Long warehouseId,
            @PathVariable Long receiptId)
    {
        var response = goodReceiptService.getGoodReceipt(warehouseId, receiptId);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_GOOD_RECEIPTS_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "PROCESS_RECEIPT", "ALL_PERMISSIONS"})
    @PostMapping("{warehouseId}/good-receipts/{receiptId}/change-status")
    public ResponseEntity<?> changeStatus(
        @PathVariable Long warehouseId,
        @PathVariable Long receiptId,
        ChangeReceiptStatusRequest request
    ) {
        goodReceiptService.changeGoodReceiptStatus(warehouseId, receiptId, request.getStatus());
        WarehouseSuccessCode code = WarehouseSuccessCode.CHANGE_GOOD_RECEIPT_STATUS_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "PROCESS_RECEIPT", "GROUP_RECEIPT", "ALL_PERMISSIONS"})
    @PostMapping("{warehouseId}/group-receipts")
    public ResponseEntity<?> processGroupReceipts(
        @PathVariable Long warehouseId,
        @RequestBody @Valid GroupReceiptRequest request
    ) {
        groupReceiptService.processGroupReceipts(warehouseId, request);
        WarehouseSuccessCode code = WarehouseSuccessCode.CREATE_GROUP_RECEIPT_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_RECEIPT", "GROUP_RECEIPT", "ALL_PERMISSIONS"})
    @GetMapping("{warehouseId}/group-receipts")
    public ResponseEntity<?> getGroupReceipts(
        @PathVariable Long warehouseId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        var response = groupReceiptService.getGroupReceipts(warehouseId, keyword, status, page, size);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_GROUP_RECEIPT_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "CANCEL_RECEIPT", "ALL_PERMISSIONS"})
    @PostMapping("{warehouseId}/good-receipts/{receiptId}/cancel")
    public ResponseEntity<?> cancelReceipt(
        @PathVariable Long warehouseId,
        @PathVariable Long receiptId
    ) {
        goodReceiptService.cancelGoodReceiptStatus(warehouseId, receiptId);
        WarehouseSuccessCode code = WarehouseSuccessCode.CANCEL_GOOD_RECEIPT_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "CANCEL_RECEIPT", "GROUP_RECEIPT", "ALL_PERMISSIONS"})
    @PostMapping("{warehouseId}/group-receipts/{groupId}/cancel")
    public ResponseEntity<?> cancelGroupReceipt(
        @PathVariable Long warehouseId,
        @PathVariable Long groupId
    ) {
        groupReceiptService.cancelGroupStatus(warehouseId, groupId);
        WarehouseSuccessCode code = WarehouseSuccessCode.CANCEL_GOOD_RECEIPT_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "COMPLETE_RECEIPT", "GROUP_RECEIPT", "ALL_PERMISSIONS"})
    @PostMapping("{warehouseId}/group-receipts/{groupId}/complete")
    public ResponseEntity<?> completeGroupReceipt(
        @PathVariable Long warehouseId,
        @PathVariable Long groupId
    ) {
        groupReceiptService.completeGroupStatus(warehouseId, groupId);
        WarehouseSuccessCode code = WarehouseSuccessCode.COMPLETE_GROUP_RECEIPT_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }
}
