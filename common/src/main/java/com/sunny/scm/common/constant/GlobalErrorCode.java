package com.sunny.scm.common.constant;


import com.sunny.scm.common.base.BaseCodeError;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;


@FieldDefaults(level = AccessLevel.PRIVATE)
public enum GlobalErrorCode implements BaseCodeError {
    UNCATEGORIZED_EXCEPTION("90000", "uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED("90001", "unauthenticated", HttpStatus.UNAUTHORIZED),
    ENDPOINT_API_NOT_FOUND("90002", "endpoint API not found in system", HttpStatus.NOT_FOUND),
    REQUEST_BODY_INVALID("90003", "request body invalid", HttpStatus.BAD_REQUEST),
    FILE_PROCESSING_ERROR("90004", "file processing error", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_PERMITTED("90005", "not permitted", HttpStatus.FORBIDDEN),
    METHOD_NOT_ALLOWED("90006", "method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
    FILE_NOT_FOUND("90007", "file not found", HttpStatus.NOT_FOUND),
    FILE_CANNOT_DELETE("90008", "file cannot delete", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_CANNOT_UPLOAD("90009", "file cannot upload", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_CANNOT_DOWNLOAD("90010", "file cannot download", HttpStatus.INTERNAL_SERVER_ERROR);

    String code;
    String message;
    HttpStatus httpStatus;

    GlobalErrorCode(String code, String message, HttpStatus httpStatus) {
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
