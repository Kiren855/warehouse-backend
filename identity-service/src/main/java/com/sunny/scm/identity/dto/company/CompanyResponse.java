package com.sunny.scm.identity.dto.company;


import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponse {
    String name;
    String legalName;
    String taxId;
    String email;
    String address;
    String phone;
}
