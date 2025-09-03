package com.sunny.scm.identity.dto.group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GroupResponse {
    Long id;
    String groupName;
}
