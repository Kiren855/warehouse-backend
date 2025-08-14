package com.sunny.scm.identity.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.identity.dto.param.CredentialParam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    String email;
    boolean emailVerified;
    boolean enabled;

    @JsonProperty("company_id")
    String companyId;
    List<CredentialParam> credentials;
}
