package com.sunny.scm.identity.dto.param;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleParam {
    String id;
    String name;
}
