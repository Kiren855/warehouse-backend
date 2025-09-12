package com.sunny.scm.product.repository;

import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    long countByCategory(Category category);
}
