package com.sunny.scm.logging.service.impl;

import com.sunny.scm.logging.dto.LogResponse;
import com.sunny.scm.logging.repository.LogRepository;
import com.sunny.scm.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
}
