package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.CodeSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CodeSequenceRepository extends JpaRepository<CodeSequence, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CodeSequence c WHERE c.scopeType = :scopeType AND c.scopeId = :scopeId")
    Optional<CodeSequence> findByScopeTypeAndScopeIdForUpdate(@Param("scopeType") String scopeType,
                                                              @Param("scopeId") Long scopeId);

}
