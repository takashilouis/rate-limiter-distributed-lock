package com.lec3.redis.service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RateLimiterService {

    private static final int REQUEST_LIMIT = 30;
    private static final int TIME_WINDOW = 60; // seconds

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(String ipAddress) {
        String key = "rate:limit:" + ipAddress;
        long currentTime = Instant.now().getEpochSecond();
        long windowStart = currentTime - TIME_WINDOW;

        Long requestCount = redisTemplate.opsForZSet().count(key, windowStart, currentTime);
        log.info("IP: {}, Requests in the last {} seconds: {}", ipAddress, TIME_WINDOW, requestCount);

        if (requestCount != null && requestCount >= REQUEST_LIMIT) {
            log.warn("Rate limit exceeded for IP: {}", ipAddress);
            return false;
        }

        redisTemplate.opsForZSet().add(key, String.valueOf(currentTime), currentTime);
        redisTemplate.expire(key, TIME_WINDOW, TimeUnit.SECONDS);
        log.info("Allowed request from IP: {}", ipAddress);
        return true;
    }
}
