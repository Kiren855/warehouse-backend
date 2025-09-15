package com.sunny.scm.warehouse.constant;

import com.sunny.scm.common.base.BaseCodeError;
import org.springframework.http.HttpStatus;

public enum WarehouseSuccessCode implements BaseCodeError {
    CREATE_WAREHOUSE_SUCCESS("S30001", "Create warehouse success", HttpStatus.OK),
    UPDATE_WAREHOUSE_SUCCESS("S30002", "Update warehouse success", HttpStatus.OK),
    DELETE_WAREHOUSE_SUCCESS("S30003", "Delete warehouse success", HttpStatus.OK),
    GET_WAREHOUSES_SUCCESS("S30004", "Get warehouses success", HttpStatus.OK),
    GET_ZONES_SUCCESS("S30006", "Get zones success", HttpStatus.OK),
    CREATE_ZONE_SUCCESS("S30007", "Create zone success", HttpStatus.OK),
    UPDATE_ZONE_SUCCESS("S30008", "Update zone success", HttpStatus.OK),
    DELETE_ZONE_SUCCESS("S30009", "Delete zone success", HttpStatus.OK),
    GET_WAREHOUSE_SUCCESS("S30010", "Get warehouse success", HttpStatus.OK),
    GET_ZONE_SUCCESS("S30011", "Get zone success", HttpStatus.OK),
    CREATE_BIN_SUCCESS("S30012", "Create bin success", HttpStatus.OK),
    UPDATE_BIN_SUCCESS("S30013", "Update bin success", HttpStatus.OK),
    DELETE_BIN_SUCCESS("S30014", "Delete bin success", HttpStatus.OK),
    GET_BINS_SUCCESS("S30015", "Get bins success", HttpStatus.OK),
    GET_BIN_SUCCESS("S30016", "Get bin success", HttpStatus.OK);
    String code;
    String message;
    HttpStatus httpStatus;
    WarehouseSuccessCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
