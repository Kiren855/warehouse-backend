package com.sunny.scm.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "companies")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime creationTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateTimestamp;

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
