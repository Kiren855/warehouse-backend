package com.sunny.scm.common.constant;


import com.sunny.scm.common.base.BaseCodeError;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;


@FieldDefaults(level = AccessLevel.PRIVATE)
public enum GlobalErrorCode implements BaseCodeError {
    UNCATEGORIZED_EXCEPTION(99999, "uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(99991, "unauthenticated", HttpStatus.UNAUTHORIZED),
    ENDPOINT_API_NOT_FOUND(99992, "endpoint API not found in system", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatus httpStatus;

    GlobalErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    @Override
    public int getCode() {
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
