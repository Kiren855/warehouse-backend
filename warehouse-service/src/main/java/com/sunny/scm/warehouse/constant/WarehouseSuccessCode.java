package com.sunny.scm.warehouse.constant;

import com.sunny.scm.common.base.BaseCodeError;
import org.springframework.http.HttpStatus;

public enum WarehouseSuccessCode implements BaseCodeError {
    CREATE_WAREHOUSE_SUCCESS("S30001", "Create warehouse success", HttpStatus.OK),
    UPDATE_WAREHOUSE_SUCCESS("S30002", "Update warehouse success", HttpStatus.OK),
    DELETE_WAREHOUSE_SUCCESS("S30003", "Delete warehouse success", HttpStatus.OK)
    ;
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
