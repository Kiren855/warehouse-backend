package com.sunny.scm.logging.service;

import com.sunny.scm.logging.dto.LogResponse;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

public interface LogService {
    List<LogResponse> getLatestLogs(LocalDateTime start, LocalDateTime end);
    List<LogResponse> getOlderLogs(LocalDateTime lastTimestamp);

    List<LogResponse> getLogs(LocalDateTime start, LocalDateTime end);
    void exportLogsToCsv(LocalDateTime start, LocalDateTime end, PrintWriter writer);
}
