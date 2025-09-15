package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.Bin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BinRepository extends JpaRepository<Bin, Long>, JpaSpecificationExecutor<Bin> {
}
