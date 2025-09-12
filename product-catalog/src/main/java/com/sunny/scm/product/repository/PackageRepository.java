package com.sunny.scm.product.repository;

import com.sunny.scm.product.entity.ProductPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<ProductPackage, Long> {
}
