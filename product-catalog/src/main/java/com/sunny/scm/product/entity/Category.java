package com.sunny.scm.product.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Category extends BaseEntity {
    @Column(name = "company_id", nullable = false)
    Long companyId;

    @Column(name = "category_name", nullable = false, unique = true)
    String categoryName;
    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "parent_category_id")
    Long parentCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> childCategories = new HashSet<>();
}
