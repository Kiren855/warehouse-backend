package com.sunny.scm.identity.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenExchangeResponse {
    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("expires_in")
    String expiresIn;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("refresh_token")
    String refreshToken;
}
