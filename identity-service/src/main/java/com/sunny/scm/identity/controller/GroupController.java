package com.sunny.scm.identity.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.dto.group.*;
import com.sunny.scm.identity.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity/api/v1/groups")
@RequiredArgsConstructor
@SuppressWarnings("unused")
@Slf4j
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
        if(!request.getRoles().isEmpty()) {
            groupService.addRolesInGroup(groupId, request);
        }
        IdentitySuccessCode successCode = IdentitySuccessCode.ROLE_ADD_IN_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @PostMapping("/{groupId}/users")
    public ResponseEntity<?> addUsersInGroup(@PathVariable Long groupId, @RequestBody UsersInGroupRequest request) {
        if(!request.getUsers().isEmpty()) {
            groupService.addUsersInGroup(groupId, request);
        }
        IdentitySuccessCode successCode = IdentitySuccessCode.USER_ADD_IN_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @Valid @RequestBody UpdateGroupRequest request) {
        groupService.updateGroup(groupId, request);
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

    @DeleteMapping("/{groupId}/users")
    public ResponseEntity<?> removeUsersFromGroup(@PathVariable Long groupId,
    @RequestBody UsersInGroupRequest request) {
        groupService.removeUsersFromGroup(groupId, request);
        IdentitySuccessCode successCode = IdentitySuccessCode.USER_DELETE_FROM_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build());
    }

    @GetMapping("/{groupId}/roles")
    public ResponseEntity<?> getRolesInGroup(@PathVariable Long groupId,
    @RequestParam (defaultValue = "0") int page,
    @RequestParam (defaultValue = "10") int size) {
        IdentitySuccessCode successCode = IdentitySuccessCode.ROLE_GET_IN_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .result(groupService.getRolesInGroup(groupId, page, size))
                .build());
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<?> getUsersInGroup(@PathVariable Long groupId,
                                             @RequestParam (defaultValue = "0") int page,
                                             @RequestParam (defaultValue = "10") int size) {
        IdentitySuccessCode successCode = IdentitySuccessCode.USER_GET_IN_GROUP_SUCCESS;

        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .result( groupService.getUsersInGroup(groupId, page, size))
                .build());
    }

    @GetMapping()
    public ResponseEntity<?> getGroups(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        IdentitySuccessCode successCode = IdentitySuccessCode.GET_GROUP_SUCCESS;
        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .result(groupService.getGroups(page, size))
                .build());
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Long groupId) {
        IdentitySuccessCode successCode = IdentitySuccessCode.GET_GROUP_SUCCESS;
        return ResponseEntity.status(successCode.getHttpStatus())
            .body(ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .result(groupService.getGroup(groupId))
                .build());
    }
}
