package com.sunny.scm.logging.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_logs")
@Getter
@Setter
@Builder
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

    @Column(name = "company_id")
    Long companyId;

    @Column(name = "activity", nullable = false, columnDefinition = "TEXT")
    String activity;
}
