package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.WarehouseSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SequenceRepository extends JpaRepository<WarehouseSequence, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ws from WarehouseSequence ws where ws.companyId = :companyId")
    Optional<WarehouseSequence> findByCompanyIdForUpdate(@Param("companyId") Long companyId);
}
