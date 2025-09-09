package com.sunny.scm.logging.service;

import com.sunny.scm.logging.dto.LogResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface LogService {
    List<LogResponse> getLatestLogs(LocalDateTime start, LocalDateTime end);
    List<LogResponse> getOlderLogs(LocalDateTime lastTimestamp);
}
