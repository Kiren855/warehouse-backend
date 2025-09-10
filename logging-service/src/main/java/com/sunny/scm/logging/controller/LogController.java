package com.sunny.scm.logging.controller;

import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.logging.service.LogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
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
                .code("E000000")
                .message("Latest logs fetched successfully")
                .result(logService.getLatestLogs(start, end))
                .build()
        );
    }

    @GetMapping("/older")
    public ResponseEntity<?> getOlderLogs(@RequestParam LocalDateTime last) {

        return ResponseEntity.status(200).body(
            ApiResponse.builder()
                .code("E00000")
                .message("Older logs fetched successfully")
                .result(logService.getOlderLogs(last))
                .build()
        );
    }

    @GetMapping("/export")
    public void getLogs(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"logs.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            logService.exportLogsToCsv(start, end, writer);
        }
    }
}
