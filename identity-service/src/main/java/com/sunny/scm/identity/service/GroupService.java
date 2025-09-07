package com.sunny.scm.identity.service;

import com.sunny.scm.common.dto.PageResponse;
import com.sunny.scm.identity.dto.auth.UsersResponse;
import com.sunny.scm.identity.dto.group.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GroupService {
    GroupResponse getGroup(Long groupId);
    PageResponse<GroupResponse> getGroups(int page, int size);

    PageResponse<UsersResponse> getUsersInGroup(Long groupId, int page, int size);
    PageResponse<RoleDetailResponse> getRolesInGroup(Long groupId, int page, int size);
    void createGroup(CreateGroupRequest request);
    void deleteGroup(Long groupId);
    void updateGroup(Long groupId, UpdateGroupRequest request);

    void addUserInGroup(Long groupId, String userId);
    void addRolesInGroup(Long groupId, RolesInGroupRequest request);
    void addUsersInGroup(Long groupId, UsersInGroupRequest request);

    void removeRoleFromGroup(Long groupId, RolesInGroupRequest request);
    void removeUsersFromGroup(Long groupId, UsersInGroupRequest request);
}
