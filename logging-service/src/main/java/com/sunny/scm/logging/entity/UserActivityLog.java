package com.sunny.scm.logging.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_logs",
        indexes = {
                @Index(name = "idx_company_creation", columnList = "company_id, created_at DESC")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime creationTimestamp;

    @Column(name = "user_id")
    String userId;

    @Column(name = "username")
    String username;

    @Column(name = "company_id")
    Long companyId;

    @Column(name = "activity", nullable = false, columnDefinition = "TEXT")
    String activity;
}
