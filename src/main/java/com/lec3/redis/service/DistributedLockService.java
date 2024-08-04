package com.lec3.redis.service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DistributedLockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean tryLock(String lockKey, long timeout, TimeUnit unit) {
        long expireTime = unit.toSeconds(timeout);
        long currentTime = Instant.now().getEpochSecond();
        String lockValue = String.valueOf(currentTime + expireTime + 1);

        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue);
        if (Boolean.TRUE.equals(success)) {
            redisTemplate.expire(lockKey, timeout, unit);
            return true;
        }

        String currentValue = redisTemplate.opsForValue().get(lockKey);
        if (currentValue != null && Long.parseLong(currentValue) < Instant.now().getEpochSecond()) {
            String oldValue = redisTemplate.opsForValue().getAndSet(lockKey, lockValue);
            if (oldValue != null && oldValue.equals(currentValue)) {
                redisTemplate.expire(lockKey, timeout, unit);
                return true;
            }
        }
        return false;
    }

    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}
