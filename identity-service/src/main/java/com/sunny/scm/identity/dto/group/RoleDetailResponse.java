package com.sunny.scm.identity.dto.group;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDetailResponse {
    Long id;
    @JsonProperty("role_name")
    String roleName;
    String description;
}
