package com.sunny.scm.identity.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateGroupRequest {
    @NotBlank(message = "Group name cannot be blank")
    @JsonProperty("group_name")
    String groupName;
}
