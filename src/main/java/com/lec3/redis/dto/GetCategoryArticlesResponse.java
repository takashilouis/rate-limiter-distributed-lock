package com.lec3.redis.dto;

import java.util.List;

import com.lec3.redis.model.Article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetCategoryArticlesResponse {
    private List<Article> articles;
}
