package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.service.RoleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity/api/v1/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<?> getRoles(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        IdentitySuccessCode successCode = IdentitySuccessCode.GET_ROLES_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(successCode.getCode())
                        .message(successCode.getMessage())
                        .result(roleService.getRoles(page, size))
                        .build());
    }
}
