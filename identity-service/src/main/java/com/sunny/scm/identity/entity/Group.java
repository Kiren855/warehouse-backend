package com.sunny.scm.identity.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"company_id", "group_name"}),
        },
        indexes = {
            @Index(name = "idx_group_name", columnList = "group_name"),
            @Index(name = "idx_group_company_id", columnList = "company_id")
        })
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group extends BaseEntity {

    @Column(name = "company_id")
    Long companyId;

    @Column(name = "group_name")
    String groupName;

    @Column(name = "created_by")
    String createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_users",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_roles",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;
}
