package com.lec3.redis.dto;

import java.util.List;

import com.lec3.redis.model.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetCategoryResponse {
    private List<Category> categories;
}
