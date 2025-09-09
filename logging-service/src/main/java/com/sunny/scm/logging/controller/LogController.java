package com.sunny.scm.logging.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/logging/api/v1/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestLogs(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {

        return ResponseEntity.status(200).body(
            ApiResponse.builder()
                .code("LOGS_FETCH_SUCCESS")
                .message("Latest logs fetched successfully")
                .result(logService.getLatestLogs(start, end))
                .build()
        );
    }

    @GetMapping("/older")
    public ResponseEntity<?> getOlderLogs(@RequestParam LocalDateTime last) {

        return ResponseEntity.status(200).body(
            ApiResponse.builder()
                .code("LOGS_FETCH_SUCCESS")
                .message("Older logs fetched successfully")
                .result(logService.getOlderLogs(last))
                .build()
        );
    }
}
