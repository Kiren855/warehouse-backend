package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.BinContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface BinContentRepository extends JpaRepository<BinContent, Long> {

    @Query("SELECT b FROM BinContent b " +
            "WHERE b.bin.id = :binId " +
            "AND b.productPackageId = :productPackageId " +
            "AND ((:expirationDate IS NULL AND b.expirationDate IS NULL) OR b.expirationDate = :expirationDate)")
    Optional<BinContent> findByBinIdAndProductPackageIdAndExpirationDate(
            @Param("binId") Long binId,
            @Param("productPackageId") Long productPackageId,
            @Param("expirationDate") LocalDate expirationDate
    );
}
