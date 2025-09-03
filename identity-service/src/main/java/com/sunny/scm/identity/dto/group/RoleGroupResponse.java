package com.sunny.scm.identity.dto.group;

import lombok.Builder;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RoleGroupResponse {
    List<RoleResponse> roles;
}
