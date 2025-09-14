package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "code_sequences")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CodeSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scope_type", nullable = false)
    private String scopeType; // WAREHOUSE, ZONE, BIN

    @Column(name = "scope_id")
    private Long scopeId;

    @Column(name = "next_val", nullable = false)
    private Long nextVal;
}
