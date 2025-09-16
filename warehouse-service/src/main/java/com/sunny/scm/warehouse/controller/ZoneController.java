package com.sunny.scm.warehouse.controller;


import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.grpc_common.aop.CheckPermission;
import com.sunny.scm.warehouse.constant.WarehouseSuccessCode;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.dto.warehouse.UpdateWarehouseRequest;
import com.sunny.scm.warehouse.dto.zone.CreateZoneRequest;
import com.sunny.scm.warehouse.dto.zone.UpdateZoneRequest;
import com.sunny.scm.warehouse.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/warehouse/api/v1/warehouses")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_ZONE", "ALL_PERMISSIONS"})
    @GetMapping("/{warehouseId}/zones")
    public ResponseEntity<?> getZones(
            @PathVariable Long warehouseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedTo,
            @RequestParam(required = false) String zoneType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationTimestamp,desc") String sort)
    {
        ZoneType type = null;
        if (zoneType != null && !zoneType.isBlank()) {
            type = ZoneType.valueOf(zoneType.toUpperCase());
        }

        var response = zoneService.getZones(warehouseId, keyword, type, updatedFrom, updatedTo, page, size, sort);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_ZONES_SUCCESS;

        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "VIEW_ZONE", "ALL_PERMISSIONS"})
    @GetMapping("/{warehouseId}/zones/{zoneId}")
    public ResponseEntity<?> getZone(
            @PathVariable Long warehouseId,
            @PathVariable Long zoneId)
    {
        var response = zoneService.getZone(warehouseId, zoneId);
        WarehouseSuccessCode code = WarehouseSuccessCode.GET_ZONE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .result(response)
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "CREATE_ZONE", "ALL_PERMISSIONS"})
    @PostMapping("/{warehouseId}/zones")
    public ResponseEntity<?> createZone(
            @PathVariable Long warehouseId,
            @Valid @RequestBody CreateZoneRequest request)
    {
        zoneService.createZone(warehouseId, request);
        WarehouseSuccessCode code = WarehouseSuccessCode.CREATE_ZONE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "UPDATE_ZONE", "ALL_PERMISSIONS"})
    @PatchMapping("/{warehouseId}/zones/{zoneId}")
    public ResponseEntity<?> updateZone(
            @PathVariable Long warehouseId,
            @PathVariable Long zoneId,
            @RequestBody UpdateZoneRequest request)
    {
        zoneService.updateZone(warehouseId, zoneId, request);
        WarehouseSuccessCode code = WarehouseSuccessCode.UPDATE_ZONE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }

    @CheckPermission(permission = {"WAREHOUSE_MANAGER", "DELETE_ZONE", "ALL_PERMISSIONS"})
    @DeleteMapping("/{warehouseId}/zones/{zoneId}")
    public ResponseEntity<?> deleteZone(
            @PathVariable Long warehouseId,
            @PathVariable Long zoneId)
    {
        zoneService.deleteZone(warehouseId, zoneId);
        WarehouseSuccessCode code = WarehouseSuccessCode.DELETE_ZONE_SUCCESS;
        return ResponseEntity.status(code.getHttpStatus())
            .body(ApiResponse.builder()
                    .code(code.getCode())
                    .message(code.getMessage())
                    .build());
    }
}
