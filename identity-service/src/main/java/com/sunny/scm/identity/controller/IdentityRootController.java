package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.service.IdentityRootService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/root/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityRootController {
    IdentityRootService identityRootService;

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<?>> hello() {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message("Hello World").build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRootRequest request) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(identityRootService.register(request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
