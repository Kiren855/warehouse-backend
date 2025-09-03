package com.sunny.scm.identity.dto.group;

import com.sunny.scm.identity.dto.auth.UsersResponse;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserGroupResponse {
    List<UsersResponse> users;
}
