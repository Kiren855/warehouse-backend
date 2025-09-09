package com.sunny.scm.logging.repository;

import com.sunny.scm.logging.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<UserActivityLog, Long> {
    List<UserActivityLog> findTop100ByCompanyIdAndCreationTimestampBetweenOrderByCreationTimestampDesc(
            Long companyId, LocalDateTime start, LocalDateTime end);

    List<UserActivityLog> findTop100ByCompanyIdAndCreationTimestampLessThanOrderByCreationTimestampDesc(
            Long companyId, LocalDateTime lastTimestamp);
}
