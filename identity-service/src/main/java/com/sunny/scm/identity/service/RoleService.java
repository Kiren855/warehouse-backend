package com.sunny.scm.identity.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.common.dto.RoleResponse;

import java.util.List;

public interface RoleService {
    PageResponse<RoleResponse> getRoles(int page, int size);

}
