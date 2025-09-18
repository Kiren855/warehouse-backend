package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.Bin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BinRepository extends JpaRepository<Bin, Long>, JpaSpecificationExecutor<Bin> {
    @Query("SELECT b FROM Bin b WHERE b.zone.warehouse.id = :warehouseId AND b.binStatus = 'ACTIVE'")
    List<Bin> findAllByWarehouseId(@Param("warehouseId") Long warehouseId);
}
