package com.sunny.scm.identity.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GroupResponse {
    Long id;
    @JsonProperty("group_name")
    String groupName;
    @JsonProperty("username_created")
    String usernameCreated;
}
