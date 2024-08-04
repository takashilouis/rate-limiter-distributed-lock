package com.lec3.redis;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.lec3.redis.model.Article;
import com.lec3.redis.model.Author;
import com.lec3.redis.model.Category;
import com.lec3.redis.repository.ArticleRepository;
import com.lec3.redis.repository.AuthorRepository;
import com.lec3.redis.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootApplication
public class RedisApplication {

	static CategoryRepository categoryRepository;
	static ArticleRepository articleRepository;
	static AuthorRepository authorRepository;

	static Category createCategory(String name){
		Category category = Category.builder().name(name).build();
		categoryRepository.save(category);
		log.info("created category {}",category.getId());
		return category;
	}
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(RedisApplication.class, args);
		categoryRepository = context.getBean(CategoryRepository.class);
		articleRepository = context.getBean(ArticleRepository.class);
		authorRepository = context.getBean(AuthorRepository.class);
		
		Category businessCategory = createCategory("Busines");
		Category sportCategory = createCategory("Spor");
	
		Author authorA = Author.builder().name("Louis Nguye").build();
		Author authorB = Author.builder().name("Khoa Nguye").build();
		Author authorC = Author.builder().name("Khoi Nguye").build();

		authorRepository.save(authorA);
		authorRepository.save(authorB);
		authorRepository.save(authorC);

		articleRepository
				.save(Article.builder().url("business-ma")
						.title("Success stor")
						.content("...").category(businessCategory)
						.authors(List.of(authorA, authorB))
						.build());

		articleRepository
				.save(Article.builder().url("socce")
						.title("Man City won PL2023/24")
						.content("...dadasd").category(businessCategory)
						.authors(List.of(authorA, authorC))
						.build());
						
		log.info("created all records");
		// list the article by an author
		//Author author = authorRepository.findById(authorB.getId()).get();
		// for (Article article : author.getArticles()) {
		// 	log.info(String.format("author B: article - %s", article.getId()));
		// }

	}

}
