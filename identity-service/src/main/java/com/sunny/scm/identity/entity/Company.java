package com.sunny.scm.identity.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity(name = "companies")
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    String name;

    @Column(name = "legal_name")
    String legalName;

    @Column(name = "tax_id", unique = true)
    String taxId;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "address", columnDefinition = "TEXT")
    String address;

    @Column(name = "phone")
    String phone;
}
