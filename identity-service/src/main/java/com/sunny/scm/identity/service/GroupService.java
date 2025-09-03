package com.sunny.scm.identity.service;

import com.sunny.scm.identity.dto.group.RolesInGroupRequest;
import com.sunny.scm.identity.dto.group.CreateGroupRequest;

public interface GroupService {
    void createGroup(CreateGroupRequest request);
    void deleteGroup(Long groupId);
    void updateGroup(Long groupId, String groupName);

    void addUserInGroup(Long groupId, String userId);
    void addRolesInGroup(Long groupId, RolesInGroupRequest request);

    void removeRoleFromGroup(Long groupId, RolesInGroupRequest request);
    void removeUserFromGroup(Long groupId, String userId);
}
