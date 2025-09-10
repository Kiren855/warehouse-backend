package com.sunny.scm.product.entity;

import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Category extends BaseEntity {
    @Column(name = "company_id", nullable = false, unique = true)
    String categoryName;
    @Column(name = "description", columnDefinition = "TEXT")
    String description;
}
