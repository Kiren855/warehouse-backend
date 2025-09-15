package com.sunny.scm.warehouse.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.warehouse.constant.WarehouseSuccessCode;
import com.sunny.scm.warehouse.dto.warehouse.CreateWarehouseRequest;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationTimestamp,desc") String sort)
    {
        var response = warehouseService.getWarehouses(keyword, createdFrom, createdTo, page, size, sort);

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

}
