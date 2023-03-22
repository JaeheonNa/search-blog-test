package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.dto.SearchRankResponseDto;
import com.kakaobank.searchblog.dto.entity.BlogSearchKeywordRanking;
import com.kakaobank.searchblog.dto.projection.BlogSearchRankingProjection;
import com.kakaobank.searchblog.repository.BlogSearchKeywordRankingRepository;
import com.kakaobank.searchblog.service.SearchRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InsertRankingServiceImpl_001 implements Runnable {
    private final BlogSearchKeywordRankingRepository blogSearchKeywordRankingRepository;

    private String query;
    public void insertSearchRanking(String query){
        BlogSearchKeywordRanking blogSearchKeywordRanking = new BlogSearchKeywordRanking(query);
        blogSearchKeywordRankingRepository.save(blogSearchKeywordRanking);
    }
    public List<BlogSearchRankingProjection> getSearchRanking() {
        List<BlogSearchRankingProjection> response = blogSearchKeywordRankingRepository.findRankingList();
        return response;
    }

    public void setQuery(String query){
        this.query = query ;
    }

    @Override
    public void run() {
        insertSearchRanking(this.query);
    }
}
