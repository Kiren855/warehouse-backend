package com.sunny.scm.product.repository;

import com.sunny.scm.product.entity.SkuSequence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkuSequenceRepository extends JpaRepository<SkuSequence, Long> {
    Optional<SkuSequence> findByCompanyId(Long companyId);
}
