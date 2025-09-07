package com.sunny.scm.common.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> stringTemplate;

    // ------------------- String -------------------
    public void setValue(String key, Object value, long ttlInSeconds) {
        stringTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlInSeconds));
    }

    public Object getValue(String key) {
        return stringTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        stringTemplate.delete(key);
    }

    // ------------------- Hash -------------------
    public boolean exists(String key) {
        return Boolean.TRUE.equals(stringTemplate.hasKey(key));
    }
}
