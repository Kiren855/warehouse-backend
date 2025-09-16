package com.sunny.scm.warehouse.entity;


import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.constant.SourceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "good_receipts",
         uniqueConstraints = {
              @UniqueConstraint(columnNames = {"company_id", "receipt_number"})
         }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoodReceipt extends BaseEntity {
    @Column(name = "company_id", nullable = false)
    Long companyId;
    @Column(name = "receipt_number", nullable = false)
    String receiptNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    SourceType sourceType;

    @Column(name = "source_id", nullable = false)
    Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "receipt_status", nullable = false)
    ReceiptStatus receiptStatus;

    @Column(name = "receipt_date")
    LocalDateTime receiptDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @OneToMany(mappedBy = "goodReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<GoodReceiptItem> items;
}
