package com.kakaobank.searchblog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;

@EnableCircuitBreaker
@SpringBootApplication
@EnableCaching
public class SearchBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchBlogApplication.class, args);
    }
}
