package com.sunny.scm.identity.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CredentialParam {
    String type;
    String value;
    boolean temporary;
}
