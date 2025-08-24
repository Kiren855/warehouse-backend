package com.sunny.scm.common.exception;


import com.sunny.scm.common.base.BaseCodeError;
import com.sunny.scm.common.constant.GlobalErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends RuntimeException {

    BaseCodeError baseCodeError;
    public AppException(BaseCodeError baseCodeError) {
        super(baseCodeError.getMessage());
        this.baseCodeError = baseCodeError;
    }
}
