package com.sunny.scm.identity.dto.auth;


import com.sunny.scm.identity.dto.auth.CredentialParam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    String email;
    boolean emailVerified;
    boolean enabled;
    List<CredentialParam> credentials;
    Map<String, List<String>> attributes;
}
