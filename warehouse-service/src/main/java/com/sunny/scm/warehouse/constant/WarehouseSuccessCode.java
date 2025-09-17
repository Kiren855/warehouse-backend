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
    GET_BIN_SUCCESS("S30016", "Get bin success", HttpStatus.OK),
    CREATE_GOOD_RECEIPT_SUCCESS("S30017", "Create good receipt success", HttpStatus.OK),
    GET_GOOD_RECEIPTS_SUCCESS("S30018", "Get good receipts success", HttpStatus.OK),
    CHANGE_GOOD_RECEIPT_STATUS_SUCCESS("S30019", "Change good receipt status success", HttpStatus.OK),
    CREATE_GROUP_RECEIPT_SUCCESS("S30020", "Create group receipt success", HttpStatus.OK),
    GET_GROUP_RECEIPT_SUCCESS("S30021", "Get group receipt success", HttpStatus.OK),
    CANCEL_GOOD_RECEIPT_SUCCESS("S30022", "Cancel good receipt success", HttpStatus.OK),
    GET_GROUPED_PACKAGES_SUCCESS("S30023", "Get grouped packages success", HttpStatus.OK),;
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
