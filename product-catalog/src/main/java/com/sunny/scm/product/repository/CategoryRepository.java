package com.sunny.scm.product.repository;

import com.sunny.scm.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
