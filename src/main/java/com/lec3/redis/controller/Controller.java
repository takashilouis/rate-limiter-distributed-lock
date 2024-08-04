package com.lec3.redis.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec3.redis.dto.GetCategoryArticlesRequest;
import com.lec3.redis.dto.GetCategoryArticlesResponse;
import com.lec3.redis.dto.GetCategoryResponse;
import com.lec3.redis.exception.CategoryNotFoundException;
import com.lec3.redis.exception.RateLimitExceededException;
import com.lec3.redis.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/category")
public class Controller {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getCategories() throws IOException {
        GetCategoryResponse response = categoryService.getCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/articles")
    public ResponseEntity<GetCategoryArticlesResponse> getArticles(@PathVariable Integer id) throws IOException { 
        log.info("request id={}",id);
        GetCategoryArticlesResponse response;
        try{
            response = categoryService.getArticles(GetCategoryArticlesRequest.builder().categoryId(id).build());
        } catch(CategoryNotFoundException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/critical")
    public ResponseEntity<String> performCriticalOperation(@PathVariable Long id) {
        log.info("Received request for critical operaion for category id={}", id);
        categoryService.performCriticalOperation(id);
        return ResponseEntity.ok("Critical operation performed for category " + id);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<String> handeRateLimitExceeded(RateLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ex.getMessage());
    }

    
}
