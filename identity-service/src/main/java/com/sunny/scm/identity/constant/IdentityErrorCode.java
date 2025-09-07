package com.sunny.scm.identity.constant;

import com.sunny.scm.common.base.BaseCodeError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public  enum IdentityErrorCode implements BaseCodeError {
    USERNAME_INVALID("E10001", "Username must be at least {min} and max {max} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID("E10003", "Username not blank", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED("E10004", "Password not blank", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_INVALID("E10005", "Company name invalid", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_REQUIRED("E10006", "COMPANY NAME REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_LEGAL_NAME_INVALID("E10007", "COMPANY LEGAL NAME INVALID", HttpStatus.BAD_REQUEST),
    COMPANY_LEGAL_NAME_REQUIRED("E10008", "COMPANY LEGAL NAME REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_TAX_INVALID("E10010", "COMPANY_TAX_REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_ADDRESS_REQUIRED("E10011", "COMPANY_ADDRESS_REQUIRED", HttpStatus.BAD_REQUEST),
    COMPANY_PHONE_INVALID("E10012", "COMPANY_PHONE_INVALID", HttpStatus.BAD_REQUEST),
    COMPANY_PHONE_REQUIRED("E10013", "COMPANY_PHONE_REQUIRED", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID("E10014", "EMAIL_INVALID", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED("E10015", "EMAIL_REQUIRED", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXISTS("E10016", "account not exist", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_ERROR("E10017", "refresh token error", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS("E10018", "user not exists in system", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_EXISTS("E10019", "company not exists in system", HttpStatus.BAD_REQUEST),
    GROUP_NOT_EXISTS("E10020", "group not exists in system", HttpStatus.BAD_REQUEST),
    LOGOUT_ERROR("E10021", "logout error", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIAL("E10022", "invalid credential", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS("E10023", "username already exists", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS("E10024", "username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("E10025", "email already exists", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_EXISTS("E10026", "account already exists", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_ALREADY_EXISTS("E10027", "company name already exists", HttpStatus.BAD_REQUEST),
    COMPANY_TAX_ID_ALREADY_EXISTS("E10028", "company tax id already exists", HttpStatus.BAD_REQUEST),
    GROUP_ALREADY_EXISTS("E10029", "group name already exists in company", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND("E10030", "role not found", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("E10031", "user not found", HttpStatus.BAD_REQUEST),
    ACCOUNT_DISABLED("E10032", "account disabled", HttpStatus.BAD_REQUEST),
    USERNAME_OR_PASSWORD_UNCORRECT("E10033", "username or password uncorrected", HttpStatus.BAD_REQUEST),;

    String code;
    String message;
    HttpStatus httpStatus;
    IdentityErrorCode(String code, String message, HttpStatus httpStatus) {
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
