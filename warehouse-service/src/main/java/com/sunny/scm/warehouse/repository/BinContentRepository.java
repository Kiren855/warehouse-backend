package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.BinContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BinContentRepository extends JpaRepository<BinContent, Long> {
    Optional<BinContent> findByBinIdAndProductPackageId(Long binId, Long productPackageId);
}
