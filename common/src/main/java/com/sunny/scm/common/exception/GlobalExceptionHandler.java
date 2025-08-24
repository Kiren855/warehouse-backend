package com.sunny.scm.common.exception;

import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlingRuntimeException(RuntimeException exception) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(GlobalErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(GlobalErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
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

        return ResponseEntity.status(globalErrorCode.getCode())
                .body(ApiResponse.builder()
                        .code(globalErrorCode.getCode())
                        .message(globalErrorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        GlobalErrorCode globalErrorCode = GlobalErrorCode.UNCATEGORIZED_EXCEPTION;

        Map<String, Object> attributes = null;
        try {
            globalErrorCode = GlobalErrorCode.valueOf(enumKey);

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        } catch (IllegalArgumentException ignored) {

        }

        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(globalErrorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(globalErrorCode.getMessage(), attributes)
                        : globalErrorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
