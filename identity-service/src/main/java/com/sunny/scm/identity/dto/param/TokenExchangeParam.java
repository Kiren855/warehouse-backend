package com.sunny.scm.identity.dto.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenExchangeParam {
    @JsonProperty("grant_type")
    String grantType;

    @JsonProperty("client_id")
    String client_id;

    @JsonProperty("client_secret")
    String clientSecret;

    String scope;
    String username;
    String password;

    @JsonProperty("refresh_token")
    String refreshToken;
}
