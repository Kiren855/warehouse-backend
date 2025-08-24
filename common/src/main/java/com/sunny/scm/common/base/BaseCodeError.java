package com.sunny.scm.common.base;

import org.springframework.http.HttpStatus;

public interface BaseCodeError {
    int getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
