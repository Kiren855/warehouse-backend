package com.sunny.scm.identity.dto.group;

import com.sunny.scm.common.dto.RoleResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Setter
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RoleGroupResponse {
    List<RoleResponse> roles;
}
