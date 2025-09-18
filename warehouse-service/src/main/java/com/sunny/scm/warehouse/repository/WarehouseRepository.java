package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>,
        JpaSpecificationExecutor<Warehouse> {
    long countByCompanyId(Long companyId);
    Page<Warehouse> findByCompanyId(Long companyId, Pageable pageable);

    @Query("SELECT w FROM Warehouse w " +
            "WHERE w.companyId = :companyId " +
            "AND (LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Warehouse> searchByCompanyAndKeyword(
            @Param("companyId") Long companyId,
            @Param("keyword") String keyword,
            Pageable pageable);
}
