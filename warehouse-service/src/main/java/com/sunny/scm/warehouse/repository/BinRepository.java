package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.Bin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinRepository extends JpaRepository<Bin, Long> {
}
