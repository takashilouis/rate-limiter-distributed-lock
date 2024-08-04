package com.lec3.redis.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lec3.redis.exception.RateLimitExceededException;
import com.lec3.redis.service.RateLimiterService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Before("execution(* com.lec3.redis.controller.*.*(..))")
    public void rateLimit() throws RateLimitExceededException {
        String ipAddress = getClientIp(request);
        log.info("Intercepting request from IP: {}", ipAddress);

        if (!rateLimiterService.isAllowed(ipAddress)) {
            log.warn("Blocking request from IP: {}", ipAddress);
            throw new RateLimitExceededException("Rate limit exceeded");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
