package com.sunny.scm.identity.controller;


import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.dto.request.RegisterSubRequest;
import com.sunny.scm.identity.service.IdentitySubService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sub/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentitySubController {
    IdentitySubService identitySubService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterSubRequest request) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(identitySubService.register(request)).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
