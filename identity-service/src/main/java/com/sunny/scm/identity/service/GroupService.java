package com.sunny.scm.identity.service;

import com.sunny.scm.identity.dto.group.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GroupService {
    GroupResponse getGroup(Long groupId);
    Page<GroupResponse> getGroups(int page, int size);

    UserGroupResponse getUsersInGroup(Long groupId);
    RoleGroupResponse getRolesInGroup(Long groupId);
    void createGroup(CreateGroupRequest request);
    void deleteGroup(Long groupId);
    void updateGroup(Long groupId, UpdateGroupRequest request);

    void addUserInGroup(Long groupId, String userId);
    void addRolesInGroup(Long groupId, RolesInGroupRequest request);

    void removeRoleFromGroup(Long groupId, RolesInGroupRequest request);
    void removeUserFromGroup(Long groupId, String userId);
}
