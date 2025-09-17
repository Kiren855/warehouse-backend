package com.sunny.scm.identity.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companies")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company extends BaseEntity {

    @Column(name = "name", unique = true)
    String name;

    @Column(name = "legal_name")
    String legalName;

    @Column(name = "tax_id", unique = true)
    String taxId;

    @Column(name = "address", columnDefinition = "TEXT")
    String address;

    @Column(name = "phone")
    String phone;
}
