package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentityErrorCode;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        IdentitySuccessCode successCode = IdentitySuccessCode.GET_USER_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .result(userService.getUsers(page, size))
                .build());
    }



    @GetMapping("/{userId}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable String userId) {
        IdentitySuccessCode successCode = IdentitySuccessCode.GET_USER_ROLES_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .result(userService.getUserRoles(userId))
                .build());
    }
}
