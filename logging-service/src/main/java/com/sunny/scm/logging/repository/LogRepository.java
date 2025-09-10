package com.sunny.scm.logging.repository;

import com.sunny.scm.logging.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public interface LogRepository extends JpaRepository<UserActivityLog, Long> {
    List<UserActivityLog> findTop100ByCompanyIdAndCreationTimestampBetweenOrderByCreationTimestampDesc(
            Long companyId, LocalDateTime start, LocalDateTime end);

    List<UserActivityLog> findTop100ByCompanyIdAndCreationTimestampLessThanOrderByCreationTimestampDesc(
            Long companyId, LocalDateTime lastTimestamp);

    List<UserActivityLog> findByCompanyIdAndCreationTimestampBetweenOrderByCreationTimestampDesc(
            Long companyId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT l FROM UserActivityLog l WHERE l.companyId = :companyId AND l.creationTimestamp BETWEEN :start AND :end ORDER BY l.creationTimestamp DESC")
    Stream<UserActivityLog> streamLogs(@Param("companyId") Long companyId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);
}
