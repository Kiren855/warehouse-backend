package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "good_issue_items")
public class GoodIssueItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "good_issue_id", nullable = false)
    GoodIssue goodIssue;

    @Column(name = "product_package_id", nullable = false)
    Long productPackageId;

    Integer quantity;
}
