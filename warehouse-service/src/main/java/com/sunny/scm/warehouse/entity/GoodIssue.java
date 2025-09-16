package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.warehouse.constant.DestinationType;
import com.sunny.scm.warehouse.constant.IssueStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "good_issues")
public class GoodIssue extends BaseEntity {
    @Column(name = "company_id", nullable = false)
    Long companyId;

    @Column(name = "issue_number", nullable = false)
    String issueNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_type", nullable = false)
    DestinationType destinationType;

    @Column(name = "destination_id", nullable = false)
    Long destinationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_status", nullable = false)
    IssueStatus issueStatus;

    @Column(name = "issue_date")
    LocalDateTime issueDate;
}
