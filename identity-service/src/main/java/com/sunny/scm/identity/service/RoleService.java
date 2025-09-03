package com.sunny.scm.identity.service;

import java.util.List;

public interface RoleService {
    void createUserRoles(String userId);
    List<String> getUserRoles();

    void removeUserRoles();

    boolean hasRole(String roleName);
}
