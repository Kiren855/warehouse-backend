package com.sunny.scm.identity.constant;

import com.sunny.scm.common.base.BaseCodeError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum IdentitySuccessCode implements BaseCodeError {
    LOGIN_SUCCESS("S10001", "login successfully", HttpStatus.OK),
    REGISTER_SUCCESS("S10002", "register successfully", HttpStatus.CREATED),
    REFRESH_TOKEN_SUCCESS("S10003", "refresh token successfully", HttpStatus.OK)
    ;
    String code;
    String message;
    HttpStatus httpStatus;
    IdentitySuccessCode(String code, String message, HttpStatus httpStatus) {
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
