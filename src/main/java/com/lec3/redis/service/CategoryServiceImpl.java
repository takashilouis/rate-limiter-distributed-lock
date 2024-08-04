package com.lec3.redis.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec3.redis.dto.GetCategoryArticlesRequest;
import com.lec3.redis.dto.GetCategoryArticlesResponse;
import com.lec3.redis.dto.GetCategoryResponse;
import com.lec3.redis.exception.CategoryNotFoundException;
import com.lec3.redis.model.Category;
import com.lec3.redis.repository.ArticleRepository;
import com.lec3.redis.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final int CACHE_TIME_IN_MINUTE = 5;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DistributedLockService distributedLockService;
    
    @Override
    public GetCategoryArticlesResponse getArticles(GetCategoryArticlesRequest request)
        throws CategoryNotFoundException, IOException {
  
      String cacheKey = String.format("category:%d:articles", request.getCategoryId());
      String cachedData = redisTemplate.opsForValue().get(cacheKey);
      GetCategoryArticlesResponse response;
      if (Objects.isNull(cachedData)) {
        log.info("cache miss, get from mysql");
        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());
        if (optionalCategory.isEmpty()) {
          throw new CategoryNotFoundException();
        }
        Category category = optionalCategory.get();
        response = GetCategoryArticlesResponse.builder().articles(articleRepository.findByCategory(category))
            .build();
        redisTemplate.opsForValue().set(cacheKey,
            objectMapper.writeValueAsString(response),
            Duration.ofMinutes(CACHE_TIME_IN_MINUTE));
      } else {
        log.info("cache hit");
        response = objectMapper.readValue(cachedData,
            GetCategoryArticlesResponse.class);
      }
      return response;
    }


    @Override
    public GetCategoryResponse getCategories() throws IOException {
        return GetCategoryResponse.builder().categories(categoryRepository.findAll()).build();
    }

    public void performCriticalOperation(Long categoryId) {
        String lockKey = "lock:category:" + categoryId;
        boolean lockAcquired = false;

        try {
            lockAcquired = distributedLockService.tryLock(lockKey, 10, TimeUnit.SECONDS);
            if (lockAcquired) {
                log.info("Performing critical operation for category: {}", categoryId);
                Thread.sleep(5000); // Sleep for 5 seconds
            } else {
                log.warn("Could not acquire lock for category: {}", categoryId);
            }
        } catch (InterruptedException e) {
            log.info("Interrupted Exception");
            Thread.currentThread().interrupt();
        } finally {
            if (lockAcquired) {
                distributedLockService.unlock(lockKey);
                log.info("Released lock for category: {}", categoryId);
            }
        }
    }
}
