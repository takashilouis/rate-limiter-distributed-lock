package com.lec3.redis.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lec3.redis.model.Article;
import com.lec3.redis.model.Category;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByCategory(Category category);
}
