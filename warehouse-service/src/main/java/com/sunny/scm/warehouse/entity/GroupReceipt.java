package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "group_receipts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupReceipt extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @Column(name = "group_code", nullable = false)
    String groupCode;

    @OneToMany(mappedBy = "groupReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<GoodReceipt> receipts;

    @Enumerated(EnumType.STRING)
    @Column(name = "receipt_status", nullable = false)
    ReceiptStatus receiptStatus;

    @Column(name = "putaway_list_url")
    private String putawayListUrl;
}

