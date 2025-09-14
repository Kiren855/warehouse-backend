package com.sunny.scm.warehouse.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.warehouse.constant.WarehouseSuccessCode;
import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;
import com.sunny.scm.warehouse.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

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



}
