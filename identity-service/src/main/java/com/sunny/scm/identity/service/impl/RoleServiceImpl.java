package com.sunny.scm.identity.service.impl;

import com.sunny.scm.common.service.RedisService;
import com.sunny.scm.identity.repository.UserRepository;
import com.sunny.scm.identity.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final RedisService redisService;
    private final UserRepository userRepository;


    @Override
    public List<String> getUserRoles() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userId = authentication.getName();
//
//        String key = "user_roles_" + userId;
//        Object rolesObj = redisService.getValue(key);
//        if (Objects.nonNull(rolesObj) && rolesObj instanceof List<?> roleList) {
//            if (roleList.stream().allMatch(e -> e instanceof String)) {
//                List<String> roles = new ArrayList<>();
//                for (Object item : roleList) {
//                    roles.add((String) item);
//                }
//                return roles;
//            }
//        }

        return null;
    }

    @Override
    public void removeUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        String key = "user_roles_" + userId;
        redisService.deleteKey(key);
        log.info("Removed roles for user {}", userId);
    }

    @Override
    public boolean hasRole(String roleName) {
//        List<String> roles = getUserRoles();
//        if (Objects.nonNull(roles)) {
//            return roles.contains(roleName);
//        }
        return false;
    }
}
