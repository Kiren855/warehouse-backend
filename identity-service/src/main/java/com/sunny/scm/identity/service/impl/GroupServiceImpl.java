package com.sunny.scm.identity.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.identity.constant.IdentityErrorCode;
import com.sunny.scm.identity.dto.group.CreateGroupRequest;
import com.sunny.scm.identity.entity.Group;
import com.sunny.scm.identity.entity.Role;
import com.sunny.scm.identity.repository.GroupRepository;
import com.sunny.scm.identity.repository.RoleRepository;
import com.sunny.scm.identity.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
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
                    .roles(new HashSet<>()).build();

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
    public void deleteGroup() {

    }

    @Override
    public void updateGroup() {

    }
}
