package com.sunny.scm.common.exception;

import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlingRuntimeException(Exception exception) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(GlobalErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(GlobalErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiResponse<?>> handlingNoResourceException(NoResourceFoundException exception) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(GlobalErrorCode.ENDPOINT_API_NOT_FOUND.getCode())
                .message(GlobalErrorCode.ENDPOINT_API_NOT_FOUND.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException appException) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(appException.getBaseCodeError().getCode())
                .message(appException.getMessage()).build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handlingAccessDeniedException(AccessDeniedException exception) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.UNCATEGORIZED_EXCEPTION;

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.builder()
                        .code(globalErrorCode.getCode())
                        .message(globalErrorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = fieldError.getDefaultMessage();
        GlobalErrorCode errorCode = GlobalErrorCode.REQUEST_BODY_INVALID;

        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
