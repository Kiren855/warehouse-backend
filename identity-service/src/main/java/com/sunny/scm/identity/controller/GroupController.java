package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.dto.group.CreateGroupRequest;
import com.sunny.scm.identity.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private GroupService groupService;

    @PostMapping()
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<?> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        groupService.createGroup(request);
        IdentitySuccessCode successCode = IdentitySuccessCode.GROUP_CREATE_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }
}
