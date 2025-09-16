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
@Table(name = "good_receipt_items")
public class GoodReceiptItem extends BaseEntity {
    @Column(name = "product_package_id", nullable = false)
    Long productPackageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "good_receipt_id", nullable = false)
    GoodReceipt goodReceipt;

    Integer quantity;
}
