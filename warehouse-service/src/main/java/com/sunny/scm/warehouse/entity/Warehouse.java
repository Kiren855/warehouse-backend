package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.prefs.BackingStoreException;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "warehouses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "warehouse_code"})
    },
    indexes = {
        @Index(name = "idx_warehouses_company_id", columnList = "company_id")
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warehouse extends BaseEntity {
    @Column(name = "company_id", nullable = false)
    Long companyId;

    @Column(name = "warehouse_code", nullable = false)
    String warehouseCode;

    @Column(name = "warehouse_name", nullable = false)
    String warehouseName;

    @Column(name = "location")
    String location;

}
