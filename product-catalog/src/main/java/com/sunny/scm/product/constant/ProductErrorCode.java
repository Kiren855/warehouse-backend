package com.sunny.scm.product.constant;

import com.sunny.scm.common.base.BaseCodeError;
import org.springframework.http.HttpStatus;

public enum ProductErrorCode implements BaseCodeError {
    CATEGORY_NOT_EXIST("E20001", "Category not exist", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_PRODUCTS("E20002", "Category has products, cannot delete", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_CHILDREN("E20003", "Category has child categories, cannot delete", HttpStatus.BAD_REQUEST),;
    String code;
    String message;
    HttpStatus httpStatus;
    ProductErrorCode(String code, String message, HttpStatus httpStatus) {
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
