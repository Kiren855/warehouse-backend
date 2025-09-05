package com.sunny.scm.identity.service;

import java.util.List;

public interface RoleService {
    List<String> getUserRoles();

    void removeUserRoles();

    boolean hasRole(String roleName);
}
