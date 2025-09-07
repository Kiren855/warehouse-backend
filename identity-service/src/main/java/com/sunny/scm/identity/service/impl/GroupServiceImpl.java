package com.sunny.scm.identity.service.impl;

import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.dto.RoleResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.identity.constant.IdentityErrorCode;
import com.sunny.scm.identity.dto.auth.UsersResponse;
import com.sunny.scm.identity.dto.group.*;
import com.sunny.scm.identity.entity.Group;
import com.sunny.scm.identity.entity.Role;
import com.sunny.scm.identity.entity.User;
import com.sunny.scm.identity.repository.GroupRepository;
import com.sunny.scm.identity.repository.RoleRepository;
import com.sunny.scm.identity.repository.UserRepository;
import com.sunny.scm.identity.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public GroupResponse getGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        return GroupResponse.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .build();
    }

    @Override
    public PageResponse<GroupResponse> getGroups(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Page<Group> groups = groupRepository.findAllByCompanyId(
                Long.valueOf(companyId),
                PageRequest.of(page, size)
        );

        Page<GroupResponse> mappedPage = groups.map(group -> GroupResponse.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .usernameCreated(
                        userRepository.findByUserId(group.getCreatedBy())
                                .map(User::getUsername)
                                .orElse("Unknown")
                )
                .build()
        );

        return PageResponse.from(mappedPage);
    }

    @Override
    public PageResponse<UsersResponse> getUsersInGroup(Long groupId, int page, int size) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        Page<User> users = groupRepository.findUsersByGroupId(
                groupId,
                PageRequest.of(page, size)
        );

        Page<UsersResponse> mappedPage = users.map(user -> UsersResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .isActive(user.isActive())
                .build()
        );

        return PageResponse.from(mappedPage);
    }

    @Override
    public PageResponse<RoleDetailResponse> getRolesInGroup(Long groupId, int page, int size) {
        groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        Page<Role> roles = groupRepository.findRolesByGroupId(
                groupId,
                PageRequest.of(page, size)
        );

        Page<RoleDetailResponse> mappedPage = roles.map(role -> RoleDetailResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build()
        );

        return PageResponse.from(mappedPage);

    }

    @Override
    public void createGroup(CreateGroupRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = authentication.getName();
        String companyId = jwt.getClaimAsString("company_id");

        boolean exists = groupRepository.existsByCompanyIdAndGroupName(Long.valueOf(companyId), request.getGroupName());
        if (exists) {
           throw new AppException(IdentityErrorCode.GROUP_ALREADY_EXISTS);
        }

        try {
            Group group = Group.builder()
                    .groupName(request.getGroupName())
                    .companyId(Long.valueOf(companyId))
                    .createdBy(userId)
                    .roles(new HashSet<>())
                    .users(new HashSet<>()).build();

            Set<Role> roles = request.getRoles().stream().map(
                    roleId -> roleRepository.findByIdAndIsActiveTrue(roleId)
                            .orElseThrow(() -> new AppException(IdentityErrorCode.ROLE_NOT_FOUND))
            ).collect(java.util.stream.Collectors.toSet());

            group.getRoles().addAll(roles);

            groupRepository.save(group);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation when creating group: {}", e.getMessage());
            throw new AppException(IdentityErrorCode.GROUP_ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        groupRepository.delete(group);
    }

    @Override
    public void updateGroup(Long groupId, UpdateGroupRequest request) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        group.setGroupName(request.getGroupName());
        groupRepository.save(group);
    }

    @Override
    public void addUserInGroup(Long groupId, String userId) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        group.getUsers().add(user);
        groupRepository.save(group);
    }

    @Override
    public void addRolesInGroup(Long groupId, RolesInGroupRequest request) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        Set<Role> roles = request.getRoles().stream().map(
                roleId -> roleRepository.findByIdAndIsActiveTrue(roleId)
                        .orElseThrow(() -> new AppException(IdentityErrorCode.ROLE_NOT_FOUND))
        ).collect(java.util.stream.Collectors.toSet());

        group.getRoles().addAll(roles);
        groupRepository.save(group);
    }

    @Override
    public void addUsersInGroup(Long groupId, UsersInGroupRequest request) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        Set<User> users = request.getUsers().stream().map(
                userId -> userRepository.findByUserId(userId)
                        .orElseThrow(() -> new AppException(IdentityErrorCode.USER_NOT_FOUND))
        ).collect(java.util.stream.Collectors.toSet());

        group.getUsers().addAll(users);
        groupRepository.save(group);
    }

    @Override
    public void removeRoleFromGroup(Long groupId, RolesInGroupRequest request) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        if(!request.getRoles().isEmpty()) {
            Set<Role> roles = request.getRoles().stream().map(
                    roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new AppException(IdentityErrorCode.ROLE_NOT_FOUND))
            ).collect(Collectors.toSet());

            group.getRoles().removeAll(roles);
            groupRepository.save(group);
        }
    }

    @Override
    public void removeUsersFromGroup(Long groupId, UsersInGroupRequest request) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        if(!request.getUsers().isEmpty()) {
            Set<User> users = request.getUsers().stream().map(
                    userId -> userRepository.findByUserId(userId)
                            .orElseThrow(() -> new AppException(IdentityErrorCode.USER_NOT_FOUND))
            ).collect(Collectors.toSet());

            group.getUsers().removeAll(users);
            groupRepository.save(group);
        }
    }
}
