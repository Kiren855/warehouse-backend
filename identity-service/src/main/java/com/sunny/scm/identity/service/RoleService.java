package com.sunny.scm.identity.service;

import java.util.List;

public interface RoleService {
    void createUserRoles(List<String> roles);
    List<String> getUserRoles();

    void removeUserRoles();

    boolean hasRole(String roleName);
}
