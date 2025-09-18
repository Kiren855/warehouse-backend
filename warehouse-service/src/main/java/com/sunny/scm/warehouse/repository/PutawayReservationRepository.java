package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.constant.PutawayReservationStatus;
import com.sunny.scm.warehouse.entity.Bin;
import com.sunny.scm.warehouse.entity.PutawayReservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PutawayReservationRepository extends JpaRepository<PutawayReservation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM PutawayReservation r " +
            "JOIN r.bin b " +
            "JOIN b.zone z " +
            "WHERE z.warehouse.id = :warehouseId " +
            "AND r.status = :status")
    List<PutawayReservation> findAllByWarehouseWithLock(@Param("warehouseId") Long warehouseId,
                                                        @Param("status") PutawayReservationStatus status);

    void deleteByGroupReceiptId(Long groupReceiptId);
    List<PutawayReservation> findByGroupReceiptId(Long groupReceiptId);
}
