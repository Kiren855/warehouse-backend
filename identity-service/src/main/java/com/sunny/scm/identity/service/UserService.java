package com.sunny.scm.identity.service;

import com.sunny.scm.identity.dto.auth.UsersResponse;
import com.sunny.scm.common.dto.RoleResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<UsersResponse> getUsers(int page, int size);

    List<RoleResponse> getUserRoles(String userId);
}
