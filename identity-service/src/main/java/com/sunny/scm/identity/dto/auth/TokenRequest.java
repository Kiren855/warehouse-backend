package com.sunny.scm.identity.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TokenRequest {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
