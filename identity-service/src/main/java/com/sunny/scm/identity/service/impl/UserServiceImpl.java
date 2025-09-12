package com.sunny.scm.identity.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.identity.constant.UserType;
import com.sunny.scm.identity.dto.auth.UsersResponse;
import com.sunny.scm.common.dto.RoleResponse;
import com.sunny.scm.identity.repository.UserRepository;
import com.sunny.scm.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public PageResponse<UsersResponse> getUsers(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Page<UsersResponse> users =  userRepository.findAllByCompanyIdAndUserType(
                        Long.valueOf(companyId),
                        UserType.SUB,
                        PageRequest.of(page, size))
                .map(user -> UsersResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .isActive(user.isActive())
                        .build());

        return PageResponse.from(users);
    }

    @Override
    @Cacheable(value = "user-roles", key = "#userId", unless = "#result == null || #result.isEmpty()", cacheManager = "cacheManager")
    public List<RoleResponse> getUserRoles(String userId) {
        return userRepository.findRolesByUserId(userId).stream()
                .map(role -> RoleResponse.builder()
                        .roleName(role.getRoleName())
                        .build())
                .toList();
    }

}
