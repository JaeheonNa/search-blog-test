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
public class SearchRankingServiceImpl_001 implements SearchRankingService {
    private final BlogSearchKeywordRankingRepository blogSearchKeywordRankingRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<SearchRankResponseDto> getSearchRanking() {
        List<BlogSearchRankingProjection> response = blogSearchKeywordRankingRepository.findRankingList();
        List<SearchRankResponseDto> rankingList = response.stream().map(BlogSearchRankingProjection::convertToResponseRankingDto).collect(Collectors.toList());
        for(int i = 1; i <= rankingList.size(); i++) rankingList.get(i-1).setRank(i);
        return rankingList;
    }

    @Override
    public void insertSearchRankingToRedis(String query){
        Double score = 0.0;
        try {
            redisTemplate.opsForZSet().incrementScore("ranking", query,1);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        redisTemplate.opsForZSet().incrementScore("ranking", query, score);
    }

    @Override
    public List<SearchRankResponseDto> getSearchRankingFromRedis() {
        String key = "ranking";
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9);  //score순으로 10개 보여줌
        List<SearchRankResponseDto> rankingList = typedTuples.stream().map(SearchRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
        for(int i = 1; i <= rankingList.size(); i++) rankingList.get(i-1).setRank(i);
        return rankingList;
    }
}
