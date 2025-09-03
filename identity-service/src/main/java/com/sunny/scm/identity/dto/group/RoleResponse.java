package com.sunny.scm.identity.dto.group;

import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RoleResponse {
    Long id;
    String roleName;
    String description;
}
