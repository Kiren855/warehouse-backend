package com.sunny.scm.identity.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.dto.group.RolesInGroupRequest;
import com.sunny.scm.identity.dto.group.CreateGroupRequest;
import com.sunny.scm.identity.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @Valid @RequestBody RolesInGroupRequest request) {
        groupService.addRolesInGroup(groupId, request);
        IdentitySuccessCode successCode = IdentitySuccessCode.ROLE_ADD_IN_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @RequestParam String groupName) {
        groupService.updateGroup(groupId, groupName);
        IdentitySuccessCode successCode = IdentitySuccessCode.GROUP_UPDATE_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        IdentitySuccessCode successCode = IdentitySuccessCode.GROUP_DELETE_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @DeleteMapping("/{groupId}/roles")
    public ResponseEntity<?> removeRolesFromGroup(@PathVariable Long groupId,
    @RequestBody RolesInGroupRequest request) {
        groupService.removeRoleFromGroup(groupId, request);
        IdentitySuccessCode successCode = IdentitySuccessCode.ROLE_DELETE_FROM_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<?> removeUserFromGroup(@PathVariable Long groupId, @PathVariable String userId) {
        groupService.removeUserFromGroup(groupId, userId);
        IdentitySuccessCode successCode = IdentitySuccessCode.USER_DELETE_FROM_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }
}
