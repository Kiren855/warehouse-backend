package com.sunny.scm.product.repository;

import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    long countByCategory(Category category);
    Page<Product> findAllByCompanyId(Long companyId, Pageable pageable);

}
