package com.sunny.scm.identity.service.impl;

import com.sunny.scm.common.constant.GlobalErrorCode;
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
    public Page<GroupResponse> getGroups(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Page<Group> groups = groupRepository
            .findAllByCompanyId(Long.valueOf(companyId), PageRequest.of(page, size));

        return groups.map(group -> GroupResponse.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .usernameCreated(userRepository.findByUserId(group.getCreatedBy())
                        .map(User::getUsername)
                        .orElse("Unknown"))
                .build());
    }

    @Override
    public UserGroupResponse getUsersInGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        return UserGroupResponse.builder()
                .users(group.getUsers().stream().map(
                        user -> UsersResponse.builder()
                                .userId(user.getUserId())
                                .username(user.getUsername()).build()
                ).toList())
                .build();
    }

    @Override
    public RoleGroupResponse getRolesInGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        return RoleGroupResponse.builder()
                .roles(group.getRoles().stream().map(
                        role -> RoleResponse.builder()
                                .id(role.getId())
                                .roleName(role.getRoleName())
                                .description(role.getDescription())
                                .build()
                ).toList())
                .build();
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
    public void removeRoleFromGroup(Long groupId, RolesInGroupRequest request) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

       try {
           Set<Role> roles = request.getRoles().stream().map(
                   roleId -> roleRepository.findById(roleId)
                           .orElseThrow(() -> new AppException(IdentityErrorCode.ROLE_NOT_FOUND))
           ).collect(java.util.stream.Collectors.toSet());

           group.getRoles().removeAll(roles);
           groupRepository.save(group);
       } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(GlobalErrorCode.UNCATEGORIZED_EXCEPTION);
       }
    }

    @Override
    public void removeUserFromGroup(Long groupId, String userId) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new AppException(IdentityErrorCode.GROUP_NOT_EXISTS));

        group.getUsers().remove(user);
        groupRepository.save(group);
    }
}
