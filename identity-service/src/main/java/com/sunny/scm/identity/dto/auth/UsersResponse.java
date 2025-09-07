package com.sunny.scm.identity.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UsersResponse {
    @JsonProperty("user_id")
    String userId;
    String username;
    @JsonProperty("is_active")
    Boolean isActive;
}
