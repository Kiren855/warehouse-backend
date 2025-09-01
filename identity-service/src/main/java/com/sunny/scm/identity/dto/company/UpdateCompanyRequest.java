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
    @Size(min = 5, message = "COMPANY_NAME_INVALID")
    @NotBlank(message = "COMPANY_NAME_REQUIRED")
    String companyName;

    @JsonProperty("company_legal_name")
    @Size(min = 5, message = "COMPANY_LEGAL_NAME_INVALID")
    @NotBlank(message = "COMPANY_LEGAL_NAME_REQUIRED")
    String legalName;

    @JsonProperty("company_tax_id")
    @Size(min = 5, max = 20, message = "COMPANY_TAX_INVALID")
    @NotBlank(message = "COMPANY_TAX_REQUIRED")
    String taxId;

    @JsonProperty("company_address")
    @NotBlank(message = "COMPANY_ADDRESS_REQUIRED")
    String address;

    @JsonProperty("company_phone")
    @Size(min = 8, max = 20, message = "COMPANY_PHONE_INVALID")
    @NotBlank(message = "COMPANY_PHONE_REQUIRED")
    String companyPhone;
}
