package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.dto.request.LoginRootRequest;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.dto.request.RegisterSubRequest;
import com.sunny.scm.identity.dto.request.TokenRequest;
import com.sunny.scm.identity.service.IdentityService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityController {
    IdentityService identityRootService;

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<?>> hello() {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message("Hello World").build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("root/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRootRequest request) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(identityRootService.register(request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("sub/register")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterSubRequest request) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(identityRootService.register(request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRootRequest request) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(IdentitySuccessCode.LOGIN_SUCCESS.getMessage())
                .result(identityRootService.login(request)).build();

        return ResponseEntity.status(IdentitySuccessCode.LOGIN_SUCCESS.getHttpStatus()).body(apiResponse);
    }

    @PostMapping("/exchange_token")
    public ResponseEntity<?> exchangeToken(@RequestBody TokenRequest request) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(IdentitySuccessCode.REFRESH_TOKEN_SUCCESS.getMessage())
                .result(identityRootService.refreshToken(request)).build();

        return ResponseEntity.status(IdentitySuccessCode.REFRESH_TOKEN_SUCCESS.getHttpStatus()).body(apiResponse);
    }
}
