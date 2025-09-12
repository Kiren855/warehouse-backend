package com.sunny.scm.product.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sunny.scm.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"company_id", "category_name"})
        }
)
public class Category extends BaseEntity {
    @Column(name = "company_id", nullable = false)
    Long companyId;

    @Column(name = "category_name", nullable = false)
    String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> childCategories = new HashSet<>();
}
