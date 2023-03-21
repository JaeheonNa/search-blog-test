package com.kakaobank.searchblog.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog_search_keyword_ranking", indexes = {
        @Index(name = "idx_blog_search_keyword", columnList = "keyword"),
        @Index(name = "idx_blog_search_create_dtime", columnList = "create_dtime")
})
public class BlogSearchKeywordRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long seq;

    @Column(name = "keyword")
    String keyword;

    @Column(name = "create_dtime")
    LocalDateTime create_dtime;

    public BlogSearchKeywordRanking(String keyword){
        this.keyword = keyword;
        this.create_dtime = LocalDateTime.now();
    }

}
