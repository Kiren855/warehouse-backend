package com.sunny.scm.warehouse.constant;

import com.sunny.scm.common.base.BaseCodeError;
import org.springframework.http.HttpStatus;

public enum WarehouseErrorCode implements BaseCodeError {
    WAREHOUSE_NOT_FOUND("E30001", "Warehouse not found", HttpStatus.NOT_FOUND),;
    String code;
    String message;
    HttpStatus httpStatus;
    WarehouseErrorCode(String code, String message, HttpStatus httpStatus) {
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
