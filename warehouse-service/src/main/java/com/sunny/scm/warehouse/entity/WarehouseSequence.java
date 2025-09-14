package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "warehouse_sequences")
@Getter
@Setter
public class WarehouseSequence extends BaseEntity {
    @Column(name = "company_id", nullable = false)
    Long companyId;
    @Column(name = "current_value", nullable = false)
    Long currentValue;
}
