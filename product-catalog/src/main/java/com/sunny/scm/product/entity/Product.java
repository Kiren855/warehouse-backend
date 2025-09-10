package com.sunny.scm.product.entity;

import com.sunny.scm.common.base.BaseEntity;
import com.sunny.scm.product.constant.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "products",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"company_id", "product_sku"})
    },
    indexes = {
        @Index(name = "idx_products_company_id", columnList = "company_id"),
        @Index(name = "idx_products_category_id", columnList = "category_id"),
        @Index(name = "idx_products_sku", columnList = "product_sku"),
        @Index(name = "idx_products_name", columnList = "product_name")
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    @Column(name = "company_id", nullable = false)
    Long companyId;
    @Column(name = "product_sku", nullable = false, unique = true)
    String productSku;
    @Column(name = "product_name", nullable = false)
    String productName;
    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "length", precision = 10, scale = 2, nullable = false)
    BigDecimal length;
    @Column(name = "height", precision = 10, scale = 2, nullable = false)
    BigDecimal width;
    @Column(name = "width", precision = 10, scale = 2, nullable = false)
    BigDecimal height;
    @Column(name = "weight", precision = 10, scale = 2, nullable = false)
    BigDecimal weight;
    @Column(name = "volume", precision = 14, scale = 2, nullable = false)
    BigDecimal volume;

    @Column(name = "barcode", unique = true)
    String barcode;
    @Column(name = "unit", nullable = false)
    String unit;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    ProductStatus status;

}
