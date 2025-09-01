package com.sunny.scm.identity.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"company_id", "username"})
        }
)
public class User implements Serializable {
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

    @Column(name = "user_id")
    String userId;

    @Column(name = "email",unique = true)
    String email;

    @Column(name = "username")
    String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    UserType userType;

    @Column(name = "parent_id")
    String parentId;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name = "company_id")
    Long companyId;
}
