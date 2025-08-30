package com.sunny.scm.identity.constant;

import com.sunny.scm.common.base.BaseCodeError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public  enum IdentityErrorCode implements BaseCodeError {
    USERNAME_INVALID(10001, "Username must be at least {min} and max {max} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(10002,"Password must be at least {min} and max {max} characters", HttpStatus.BAD_REQUEST),
    USERNAME_REQUIRED(10003, "Username not blank", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(10004, "Password not blank", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_INVALID(10005, "Company name invalid", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_REQUIRED(10006, "COMPANY NAME REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_LEGAL_NAME_INVALID(10007, "COMPANY LEGAL NAME INVALID", HttpStatus.BAD_REQUEST),
    COMPANY_LEGAL_NAME_REQUIRED(10008, "COMPANY LEGAL NAME REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_TAX_INVALID(10009, "COMPANY TAX INVALID", HttpStatus.BAD_REQUEST),
    COMPANY_TAX_REQUIRED(10010, "COMPANY_TAX_REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_ADDRESS_REQUIRED(10011, "COMPANY_ADDRESS_REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_PHONE_INVALID(10012, "COMPANY_PHONE_INVALID", HttpStatus.BAD_REQUEST),
    COMPANY_PHONE_REQUIRED(10013, "COMPANY_PHONE_REQUIRED", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(10014, "EMAIL_INVALID", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(10015, "EMAIL_REQUIRED", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXISTS(10016, "account not exist", HttpStatus.BAD_REQUEST)
    ;

    int code;
    String message;
    HttpStatus httpStatus;
    IdentityErrorCode(int code, String message, HttpStatus httpStatus) {
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
