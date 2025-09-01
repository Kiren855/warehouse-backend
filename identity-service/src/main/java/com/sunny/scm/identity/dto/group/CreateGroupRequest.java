package com.sunny.scm.identity.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGroupRequest {

    @NotBlank(message = "Group name must not be blank")
    @JsonProperty("group_name")
    String groupName;
}
