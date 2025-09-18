package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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

    @Column(name = "package_quantity", nullable = false)
    Integer packageQuantity;

    @Column(name = "expiration_date")
    LocalDate expirationDate;
}
