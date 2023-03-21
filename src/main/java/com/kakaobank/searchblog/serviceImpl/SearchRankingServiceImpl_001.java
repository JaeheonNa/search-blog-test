package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.dto.entity.BlogSearchKeywordRanking;
import com.kakaobank.searchblog.dto.projection.BlogSearchRankingProjection;
import com.kakaobank.searchblog.repository.BlogSearchKeywordRankingRepository;
import com.kakaobank.searchblog.service.SearchRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchRankingServiceImpl_001 implements SearchRankingService {
    private final BlogSearchKeywordRankingRepository blogSearchKeywordRankingRepository;
    @Override
    public void insertSearchRanking(String query){
        BlogSearchKeywordRanking blogSearchKeywordRanking = new BlogSearchKeywordRanking(query);
        blogSearchKeywordRankingRepository.save(blogSearchKeywordRanking);
    }
    @Override
    public List<BlogSearchRankingProjection> getSearchRanking() {
        List<BlogSearchRankingProjection> response = blogSearchKeywordRankingRepository.findRankingList();
        return response;
    }
}
