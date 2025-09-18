package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ZoneRepository extends JpaRepository<Zone, Long>, JpaSpecificationExecutor<Zone> {
    @Query("SELECT z FROM Zone z " +
            "WHERE z.warehouse.id = :warehouseId " +
            "AND (LOWER(z.zoneCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(z.zoneName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Zone> searchByWarehouseAndKeyword(
            @Param("warehouseId") Long warehouseId,
            @Param("keyword") String keyword,
            Pageable pageable);
}
