package com.sunny.scm.product.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.product.constant.PackageType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "product_packages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductPackage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_type", nullable = false)
    private PackageType packageType; // e.g., SINGLE, BOX, CARTON, PALLET

    @Column(name = "length", precision = 10, scale = 2, nullable = false)
    private BigDecimal length;
    @Column(name = "width", precision = 10, scale = 2, nullable = false)
    private BigDecimal width;
    @Column(name = "height", precision = 10, scale = 2, nullable = false)
    private BigDecimal height;
    @Column(name = "weight", precision = 10, scale = 2, nullable = false)
    private BigDecimal weight;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "quantity_in_parent")
    Integer quantityInParent;
    @Transient
    public BigDecimal getVolume() {
        if (this.length != null && this.width != null && this.height != null) {
            return this.length.multiply(this.width).multiply(this.height);
        }
        return BigDecimal.ZERO;
    }
}
