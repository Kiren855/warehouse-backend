package com.sunny.scm.identity.dto.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompanyRequest {
    @JsonProperty("company_name")
    String companyName;

    @JsonProperty("company_legal_name")
    String legalName;

    @JsonProperty("company_tax_id")
    String taxId;

    String address;

    @JsonProperty("company_phone")
    String companyPhone;
}
