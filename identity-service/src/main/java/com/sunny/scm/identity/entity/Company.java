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
    @Column(unique = true, nullable = false)
    String name;

    @Column(name = "legal_name")
    String legalName;

    @Column(name = "tax_id", unique = true)
    String taxId;

    @Column(unique = true)
    String email;

    @Column(columnDefinition = "TEXT")
    String address;

    String phone;
}
