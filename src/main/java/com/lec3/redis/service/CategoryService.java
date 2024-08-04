package com.lec3.redis.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.lec3.redis.dto.GetCategoryArticlesRequest;
import com.lec3.redis.dto.GetCategoryArticlesResponse;
import com.lec3.redis.dto.GetCategoryResponse;
import com.lec3.redis.exception.CategoryNotFoundException;

@Service
public interface CategoryService {
    GetCategoryArticlesResponse getArticles(GetCategoryArticlesRequest request) throws CategoryNotFoundException, IOException;
    GetCategoryResponse getCategories() throws IOException;
    void performCriticalOperation(Long categoryId);
}
