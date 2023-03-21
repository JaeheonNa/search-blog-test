package com.kakaobank.searchblog.service;

import com.kakaobank.searchblog.dto.projection.BlogSearchRankingProjection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchRankingService {
    void insertSearchRanking(String query);
    List<BlogSearchRankingProjection> getSearchRanking();
}
