package com.sunny.scm.common.base;

import org.springframework.http.HttpStatus;

public interface BaseCodeError {
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
