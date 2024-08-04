package com.lec3.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lec3.redis.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findOneByName(String name);
}