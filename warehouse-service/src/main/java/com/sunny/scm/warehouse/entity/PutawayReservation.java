package com.sunny.scm.warehouse.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.warehouse.constant.PutawayReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.checkerframework.checker.units.qual.N;
import reactor.core.publisher.Sinks;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "putaway_reservations",
    indexes = {
        @Index(name = "idx_putaway_group_id", columnList = "putaway_group_id")}
)
@Getter
@Setter
public class PutawayReservation extends BaseEntity {
    @Column(name = "putaway_group_id", nullable = false)
    Long putawayGroupId;
    @Column(name = "product_package_id", nullable = false)
    Long productPackageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bin_id", nullable = false)
    private Bin bin;

    @Column(name = "quantity_reserved", nullable = false)
    Integer quantityReserved;

    @Enumerated(EnumType.STRING)
    PutawayReservationStatus status;
}
