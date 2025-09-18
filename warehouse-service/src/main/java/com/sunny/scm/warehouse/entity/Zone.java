package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.warehouse.constant.ZoneType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "zones")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Zone extends BaseEntity {

    @Column(name = "zone_code", nullable = false)
    String zoneCode;
    @Column(name = "zone_name", nullable = false)
    String zoneName;

    @Enumerated(EnumType.STRING)
    @Column(name = "zone_type")
    ZoneType zoneType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

}
