package com.sunny.scm.product.repository;

import com.sunny.scm.product.entity.Category;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByParentCategory_Id(Long parentId);
    Optional<Category> findByCategoryName(String categoryName);

}
