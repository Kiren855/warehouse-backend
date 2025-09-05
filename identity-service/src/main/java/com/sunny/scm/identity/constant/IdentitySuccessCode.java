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
    REFRESH_TOKEN_SUCCESS("S10003", "refresh token successfully", HttpStatus.OK),
    LOGOUT_SUCCESS("S10004", "logout successfully", HttpStatus.OK),
    COMPANY_CREATE_SUCCESS("S10005", "create company successfully", HttpStatus.CREATED),
    COMPANY_UPDATE_SUCCESS("S10006", "update company successfully", HttpStatus.OK),
    GROUP_CREATE_SUCCESS("S10007", "create group successfully", HttpStatus.CREATED),
    USER_ADD_IN_GROUP_SUCCESS("S10008", "add user in group successfully", HttpStatus.OK),
    ROLE_ADD_IN_GROUP_SUCCESS("S10009", "add role in group successfully", HttpStatus.OK),
    GROUP_UPDATE_SUCCESS("S10010", "update group successfully", HttpStatus.OK),
    GROUP_DELETE_SUCCESS("S10011", "delete group successfully", HttpStatus.OK),
    ROLE_DELETE_FROM_GROUP_SUCCESS("S10012", "delete role from group successfully", HttpStatus.OK),
    USER_DELETE_FROM_GROUP_SUCCESS("S10013", "delete user from group successfully", HttpStatus.OK),
    ROLE_GET_IN_GROUP_SUCCESS("S10014", "get roles in group successfully", HttpStatus.OK),
    USER_GET_IN_GROUP_SUCCESS("S10015", "get users in group successfully", HttpStatus.OK),
    GET_GROUP_SUCCESS("S10016", "get group successfully", HttpStatus.OK),
    GET_USER_SUCCESS("S10017", "get users successfully", HttpStatus.OK),
    GET_USER_ROLES_SUCCESS("S10018", "get user roles successfully", HttpStatus.OK),
    GET_ROLES_SUCCESS("S10019", "get roles successfully", HttpStatus.OK),
    ROLE_CREATE_SUCCESS("S10020", "create role successfully", HttpStatus.CREATED),
    ROLE_UPDATE_SUCCESS("S10021", "update role successfully", HttpStatus.OK),
    ROLE_DELETE_SUCCESS("S10022", "delete role successfully", HttpStatus.OK),
    ASSIGN_ROLE_TO_USER_SUCCESS("S10023", "assign role to user successfully", HttpStatus.OK),
    REVOKE_ROLE_FROM_USER_SUCCESS("S10024", "revoke role from user successfully", HttpStatus.OK),;
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
