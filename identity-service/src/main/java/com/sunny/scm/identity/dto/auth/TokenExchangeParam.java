package com.sunny.scm.identity.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenExchangeParam {
    @JsonProperty("grant_type")
    String grant_type;

    @JsonProperty("client_id")
    String client_id;

    @JsonProperty("client_secret")
    String client_secret;

    String scope;
    String username;
    String password;

    @JsonProperty("refresh_token")
    String refresh_token;
}
