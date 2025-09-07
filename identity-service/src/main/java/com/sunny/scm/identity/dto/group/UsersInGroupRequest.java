package com.sunny.scm.identity.dto.group;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersInGroupRequest {
    List<String> users;
}
