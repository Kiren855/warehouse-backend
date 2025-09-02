package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.dto.group.AddRolesInGroupRequest;
import com.sunny.scm.identity.dto.group.CreateGroupRequest;
import com.sunny.scm.identity.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping()
    public ResponseEntity<?> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        groupService.createGroup(request);
        IdentitySuccessCode successCode = IdentitySuccessCode.GROUP_CREATE_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<?> addUserInGroup(@PathVariable Long groupId, @PathVariable String userId) {
        groupService.addUserInGroup(groupId, userId);
        IdentitySuccessCode successCode = IdentitySuccessCode.USER_ADD_IN_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());

    }

    @PostMapping("/{groupId}/roles")
    public ResponseEntity<?> addRolesInGroup(@PathVariable Long groupId,
    @Valid @RequestBody AddRolesInGroupRequest request) {
        groupService.addRolesInGroup(groupId, request);
        IdentitySuccessCode successCode = IdentitySuccessCode.ROLE_ADD_IN_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }
}
