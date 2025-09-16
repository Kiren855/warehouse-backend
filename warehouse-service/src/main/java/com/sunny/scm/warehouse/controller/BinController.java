package com.sunny.scm.warehouse.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.constant.WarehouseSuccessCode;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.dto.bin.CreateBinRequest;
import com.sunny.scm.warehouse.dto.bin.UpdateBinRequest;
import com.sunny.scm.warehouse.service.BinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("warehouse/api/v1/zones/{zoneId}/bins")
@RequiredArgsConstructor
public class BinController {
    private final BinService binService;

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "CREATE_BIN", "ALL_PERMISSIONS"} )
    @PostMapping()
    public ResponseEntity<?> createBin(
            @PathVariable Long zoneId,
            @Valid @RequestBody CreateBinRequest request
            )
    {
        binService.createBin(zoneId, request);
        WarehouseSuccessCode code = WarehouseSuccessCode.CREATE_BIN_SUCCESS;

        return ResponseEntity.status(code.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "UPDATE_BIN", "ALL_PERMISSIONS"} )
    @PatchMapping("/{binId}")
    public ResponseEntity<?> updateBin(
            @PathVariable Long zoneId,
            @PathVariable Long binId,
            @Valid @RequestBody UpdateBinRequest request
    )
    {
        binService.updateBin(zoneId, binId, request);
        WarehouseSuccessCode code = WarehouseSuccessCode.UPDATE_BIN_SUCCESS;

        return ResponseEntity.status(code.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_BIN", "ALL_PERMISSIONS"} )
    @GetMapping("/{binId}")
    public ResponseEntity<?> getBin(
            @PathVariable Long zoneId,
            @PathVariable Long binId
    )
    {
        var bin = binService.getBin(zoneId, binId);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_BIN_SUCCESS;

        return ResponseEntity.status(code.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(code.getCode())
                        .message(code.getMessage())
                        .result(bin)
                        .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_BIN", "ALL_PERMISSIONS"} )
    @GetMapping()
    public ResponseEntity<?> getBins(
            @PathVariable Long zoneId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedTo,
            @RequestParam(required = false) String binType,
            @RequestParam(required = false) String binStatus,
            @RequestParam(required = false) String contentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updateTimestamp,desc") String sort)
    {
        BinType type = null;
        if (binType != null && !binType.isBlank()) {
            type = BinType.valueOf(binType.toUpperCase());
        }

        BinStatus status = null;
        if (binStatus != null && !binStatus.isBlank()) {
            status = BinStatus.valueOf(binStatus.toUpperCase());
        }

        var response = binService.getBins(zoneId, keyword, type, status, contentStatus, updatedFrom, updatedTo, page, size, sort);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_BINS_SUCCESS;

        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());

    }
}
