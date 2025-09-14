package com.sunny.scm.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "sku_sequences")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkuSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "company_id", nullable = false, unique = true)
    Long companyId;
    @Column(name = "last_value", nullable = false)
    Long lastValue;
}
