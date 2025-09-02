package com.sunny.scm.identity.service;

import com.sunny.scm.identity.dto.group.CreateGroupRequest;

public interface GroupService {
    void createGroup(CreateGroupRequest request);
    void deleteGroup();
    void updateGroup();

    void addUsersInGroup()
}
