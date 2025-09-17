package com.sunny.scm.product.repository;

import com.sunny.scm.product.entity.ProductPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<ProductPackage, Long> {
    Page<ProductPackage> findAllByProductId(Long productId, Pageable pageable);
    List<ProductPackage> findAllByProductId(Long productId);
    Page<ProductPackage> findAllByIdIn(List<Long> ids, Pageable pageable);
}
