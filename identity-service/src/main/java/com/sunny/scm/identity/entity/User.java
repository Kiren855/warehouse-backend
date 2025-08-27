package com.sunny.scm.identity.entity;


import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"company_id", "username"})
        }
)
public class User extends BaseEntity {

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
    Long parentId;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name = "company_id")
    Long companyId;
}
