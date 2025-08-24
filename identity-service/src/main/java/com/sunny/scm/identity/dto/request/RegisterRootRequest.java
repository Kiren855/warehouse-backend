package com.sunny.scm.identity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRootRequest {

    @JsonProperty("company_name")
    @Size(min = 5)
    @NotBlank()
    String companyName;
    String legaName;
    String taxId;
    String companyEmail;
    String address;
    String companyPhone;
}
