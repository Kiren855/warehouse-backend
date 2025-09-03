package com.sunny.scm.identity.controller;

import com.sunny.scm.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/identity/api/v1/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisService redisService;

    @GetMapping("/set")
    public String setValue() {
        String key = "test_roles_123";
        List<String> roles = List.of("ROLE_A", "ROLE_B");

        redisService.setValue(key, roles, 300); // TTL = 300s
        return "Saved key: " + key;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getValue() {
        Object value = redisService.getValue("test_roles_123");
        if (value == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Key not found");
        }
        return ResponseEntity.ok(value);
    }
}
