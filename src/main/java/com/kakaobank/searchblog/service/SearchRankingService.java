package com.kakaobank.searchblog.service;

import com.kakaobank.searchblog.dto.SearchRankResponseDto;
import com.kakaobank.searchblog.dto.projection.BlogSearchRankingProjection;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public interface SearchRankingService {
    void insertSearchRanking(String query);
    List<BlogSearchRankingProjection> getSearchRanking();

    void insertSearchRankingToRedis(String query);
    List<SearchRankResponseDto> getSearchRankingFromRedis();
}
