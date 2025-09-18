package com.sunny.scm.warehouse.constant;

import com.sunny.scm.common.base.BaseCodeError;
import org.springframework.http.HttpStatus;

public enum WarehouseErrorCode implements BaseCodeError {
    WAREHOUSE_NOT_FOUND("E30001", "Warehouse not found", HttpStatus.NOT_FOUND),
    ZONE_NOT_FOUND("E30002", "Zone not found", HttpStatus.NOT_FOUND),
    BIN_NOT_FOUND("E30003", "Bin not found", HttpStatus.NOT_FOUND),
    RECEIPT_NOT_FOUND("E30004", "Receipt not found", HttpStatus.NOT_FOUND),
    GROUP_RECEIPT_NOT_FOUND("E30005", "Group receipt not found", HttpStatus.NOT_FOUND),
    GROUP_RECEIPT_ALREADY_CANCELLED("E30006", "Group receipt already cancelled", HttpStatus.BAD_REQUEST),
    PUTAWAY_PDF_NOT_FOUND("E30007", "Putaway PDF not found", HttpStatus.NOT_FOUND),;
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
