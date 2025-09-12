package com.sunny.scm.product.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.product.constant.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "products",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"company_id", "product_sku"}),
            @UniqueConstraint(columnNames = {"company_id", "barcode"})
    },
    indexes = {
        @Index(name = "idx_products_company_id", columnList = "company_id"),
        @Index(name = "idx_products_category_id", columnList = "category_id"),
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    @Column(name = "company_id", nullable = false)
    private Long companyId;
    @Column(name = "product_sku", nullable = false)
    private String productSku;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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
    @Column(name = "unit", nullable = false)
    private String unit;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Transient
    public BigDecimal getVolume() {
        if (this.length != null && this.width != null && this.height != null) {
            return this.length.multiply(this.width).multiply(this.height);
        }
        return BigDecimal.ZERO;
    }
}
