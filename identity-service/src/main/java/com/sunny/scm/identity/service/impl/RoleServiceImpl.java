package com.sunny.scm.identity.service.impl;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.dto.RoleResponse;
import com.sunny.scm.common.service.RedisService;
import com.sunny.scm.identity.entity.Role;
import com.sunny.scm.identity.repository.RoleRepository;
import com.sunny.scm.identity.repository.UserRepository;
import com.sunny.scm.identity.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    @Override
    public PageResponse<RoleResponse> getRoles(int page, int size) {
        Page<RoleResponse> roles = repository
            .findAllByIsActiveTrue(PageRequest.of(page, size)).map(role -> RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build());

        return PageResponse.from(roles);
    }
}
