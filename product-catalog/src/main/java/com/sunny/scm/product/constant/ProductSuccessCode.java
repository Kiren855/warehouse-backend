package com.sunny.scm.product.constant;

import com.sunny.scm.common.base.BaseCodeError;
import org.springframework.http.HttpStatus;

public enum ProductSuccessCode implements BaseCodeError {
    CREATE_PRODUCT_SUCCESS("S20001", "Create product success", HttpStatus.OK),
    UPDATE_PRODUCT_SUCCESS("S20005", "Update product success", HttpStatus.OK),
    DELETE_PRODUCT_SUCCESS("S20006", "Delete product success", HttpStatus.OK),
    CREATE_CATEGORY_SUCCESS("S20002", "Create category success", HttpStatus.OK),
    UPDATE_CATEGORY_SUCCESS("S20003", "Update category success", HttpStatus.OK),
    DELETE_CATEGORY_SUCCESS("S20004", "Delete category success", HttpStatus.OK),

    ;String code;
    String message;
    HttpStatus httpStatus;
    ProductSuccessCode(String code, String message, HttpStatus httpStatus) {
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
