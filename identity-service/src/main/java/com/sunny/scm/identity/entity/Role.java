package com.sunny.scm.identity.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {
    @Column(name = "role_name", unique = true, nullable = false)
    String roleName;

    @Column(name = "description")
    String description;

    @Column(name = "is_active")
    boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    Set<Group> groups;
}
