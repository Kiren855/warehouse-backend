package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(
        name = "bin_contents",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"bin_id", "product_package_id"})
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BinContent extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id", nullable = false)
    Bin bin;

    @Column(name = "product_package_id", nullable = false)
    Long productPackageId;

    @Column(name = "quantity", nullable = false)
    BigDecimal quantity;

    @Column(name = "expiration_date")
    LocalDate expirationDate;
}
