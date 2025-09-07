package com.sunny.scm.identity.dto.group;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssignRoleRequest {
    Long roleId;
}
