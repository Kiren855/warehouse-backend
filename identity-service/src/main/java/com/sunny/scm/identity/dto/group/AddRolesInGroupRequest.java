package com.sunny.scm.identity.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddRolesInGroupRequest {
    @NotBlank(message = "roles can't be blank")
    List<Long> roles;
}
