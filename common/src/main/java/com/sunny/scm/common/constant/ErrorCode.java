package com.sunny.scm.common.constant;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    ;
    int code;
    String message;
    HttpStatus httpStatus;
}
