package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "bins")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Bin extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "length", precision = 10, scale = 2, nullable = false)
    private BigDecimal length;

    @Column(name = "width", precision = 10, scale = 2, nullable = false)
    private BigDecimal width;

    @Column(name = "height", precision = 10, scale = 2, nullable = false)
    private BigDecimal height;

    @Column(name = "max_weight", precision = 10, scale = 2, nullable = false)
    private BigDecimal maxWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "bin_type", nullable = false)
    BinType binType;

    @Column(name = "current_volume_used", precision = 14, scale = 4, nullable = false)
    private BigDecimal currentVolumeUsed = BigDecimal.ZERO;

    @Column(name = "current_weight_used", precision = 10, scale = 2, nullable = false)
    private BigDecimal currentWeightUsed = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    BinStatus binStatus;

    @OneToMany(mappedBy = "bin", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<BinContent> binContents;
}
