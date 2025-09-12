package com.sunny.scm.common.exception;

import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<?> handlingException(Exception exception) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(GlobalErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(GlobalErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = IOException.class)
    ResponseEntity<?> handlingIOException(IOException exception) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(GlobalErrorCode.FILE_PROCESSING_ERROR.getCode())
                .message(GlobalErrorCode.FILE_PROCESSING_ERROR.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<?> handlingNoResourceException(NoResourceFoundException exception) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(GlobalErrorCode.ENDPOINT_API_NOT_FOUND.getCode())
                .message(GlobalErrorCode.ENDPOINT_API_NOT_FOUND.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<?> handlingAppException(AppException appException) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(appException.getBaseCodeError().getCode())
                .message(appException.getMessage()).build();

        return ResponseEntity.status(appException.getBaseCodeError().getHttpStatus()).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<?> handlingAccessDeniedException(AccessDeniedException exception) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.UNCATEGORIZED_EXCEPTION;

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.builder()
                        .code(globalErrorCode.getCode())
                        .message(globalErrorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<?> handlingValidation(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = fieldError.getDefaultMessage();
        GlobalErrorCode errorCode = GlobalErrorCode.REQUEST_BODY_INVALID;

        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    ResponseEntity<?> handlingMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        GlobalErrorCode errorCode = GlobalErrorCode.METHOD_NOT_ALLOWED;

        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
}
