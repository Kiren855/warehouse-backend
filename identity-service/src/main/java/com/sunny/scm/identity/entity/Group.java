package com.sunny.scm.identity.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "groups",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "group_name"})
})
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group implements Serializable {

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

    @Column(name = "company_id")
    Long companyId;

    @Column(name = "group_name")
    String groupName;

    @Column(name = "create_by")
    String createdBy;
}
