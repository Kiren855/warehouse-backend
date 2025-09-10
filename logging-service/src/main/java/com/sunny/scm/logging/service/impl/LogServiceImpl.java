package com.sunny.scm.logging.service.impl;

import com.sunny.scm.logging.dto.LogResponse;
import com.sunny.scm.logging.entity.UserActivityLog;
import com.sunny.scm.logging.repository.LogRepository;
import com.sunny.scm.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;


    @Override
    public List<LogResponse> getLatestLogs(LocalDateTime start, LocalDateTime end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        return logRepository
                .findTop100ByCompanyIdAndCreationTimestampBetweenOrderByCreationTimestampDesc(
                        Long.parseLong(companyId), start, end)
                .stream()
                .map(log1 -> LogResponse.builder()
                        .username(log1.getUsername())
                        .activity(log1.getActivity())
                        .creationTimestamp(log1.getCreationTimestamp())
                        .build())
                .toList();
    }

    @Override
    public List<LogResponse> getOlderLogs(LocalDateTime lastTimestamp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        return logRepository
                .findTop100ByCompanyIdAndCreationTimestampLessThanOrderByCreationTimestampDesc(
                        Long.parseLong(companyId), lastTimestamp)
                .stream()
                .map(log -> LogResponse.builder()
                        .username(log.getUsername())
                        .activity(log.getActivity())
                        .creationTimestamp(log.getCreationTimestamp())
                        .build())
                .toList();
    }

    @Override
    public List<LogResponse> getLogs(LocalDateTime start, LocalDateTime end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        return logRepository
                .findByCompanyIdAndCreationTimestampBetweenOrderByCreationTimestampDesc(
                        Long.parseLong(companyId), start, end)
                .stream()
                .map(log -> LogResponse.builder()
                        .username(log.getUsername())
                        .activity(log.getActivity())
                        .creationTimestamp(log.getCreationTimestamp())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public void exportLogsToCsv(LocalDateTime start, LocalDateTime end, PrintWriter writer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        // Header CSV
        writer.println("Timestamp,Username,Activity");

        try (Stream<UserActivityLog> stream = logRepository.streamLogs(Long.valueOf(companyId), start, end)) {
            stream.forEach(log -> writer.printf("%s,%s,%s\n",
                    log.getCreationTimestamp(),
                    log.getUsername(),
                    log.getActivity().replace(",", " ")));
        }
    }
}
