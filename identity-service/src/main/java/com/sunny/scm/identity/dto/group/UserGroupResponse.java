package com.sunny.scm.identity.dto.group;

import com.sunny.scm.identity.dto.auth.UsersResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserGroupResponse {
    List<UsersResponse> users;
}
