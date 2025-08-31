package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.dto.auth.*;
import com.sunny.scm.identity.service.IdentityService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class IdentityController {
    private final IdentityService identityRootService;

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRootRequest request,
                                   HttpServletResponse response) {
        TokenExchangeResponse tokenResponse = identityRootService.login(request);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(IdentitySuccessCode.LOGIN_SUCCESS.getMessage())
                .result(TokenExchangeResponse.builder()
                        .accessToken(tokenResponse.getAccessToken())
                        .expiresIn(tokenResponse.getExpiresIn())
                        .tokenType(tokenResponse.getTokenType())
                        .build())
                .build();

        return ResponseEntity
        .status(IdentitySuccessCode.LOGIN_SUCCESS.getHttpStatus())
        .body(apiResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> exchangeToken(@RequestBody TokenRequest request) {
        TokenExchangeResponse tokenResponse = identityRootService.refreshToken(request);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(IdentitySuccessCode.REFRESH_TOKEN_SUCCESS.getMessage())
                .result(TokenExchangeResponse.builder()
                        .accessToken(tokenResponse.getAccessToken())
                        .tokenType(tokenResponse.getTokenType())
                        .expiresIn(tokenResponse.getExpiresIn())
                        .build())
                .build();

        return ResponseEntity
        .status(IdentitySuccessCode.REFRESH_TOKEN_SUCCESS.getHttpStatus())
        .body(apiResponse);
    }


}
