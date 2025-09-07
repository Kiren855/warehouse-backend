package com.sunny.scm.logging.repository;

import com.sunny.scm.logging.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<UserActivityLog, Long> {
}
