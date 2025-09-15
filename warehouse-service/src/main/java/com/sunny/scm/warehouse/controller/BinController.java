package com.sunny.scm.warehouse.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.warehouse.constant.WarehouseSuccessCode;
import com.sunny.scm.warehouse.dto.bin.CreateBinRequest;
import com.sunny.scm.warehouse.dto.bin.UpdateBinRequest;
import com.sunny.scm.warehouse.service.BinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
