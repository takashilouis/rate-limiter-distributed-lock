package com.lec3.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lec3.redis.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
