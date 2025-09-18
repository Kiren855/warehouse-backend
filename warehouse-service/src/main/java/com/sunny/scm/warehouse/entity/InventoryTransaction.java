package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.warehouse.constant.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions",
        indexes = {
                @Index(name = "idx_inv_trans_company_id", columnList = "company_id"),
                @Index(name = "idx_inv_trans_product_id", columnList = "package_product_id"),
                @Index(name = "idx_inv_trans_type", columnList = "transaction_type")
        })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransaction extends BaseEntity {

    @Column(name = "company_id", nullable = false)
    Long companyId;

    @Column(name = "package_product_id", nullable = false)
    Long packageProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_bin_id")
    Bin fromBin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_bin_id")
    Bin toBin;

    @Column(name = "quantity", nullable = false)
    BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    TransactionType transactionType;

    @Column(name = "reference_doc_id", nullable = false)
    Long referenceDocId;

    @Column(name = "reference_doc_type", nullable = false)
    String referenceDocType;

    @Column(name = "transaction_date", nullable = false)
    LocalDateTime transactionDate;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "note")
    String note;
}

