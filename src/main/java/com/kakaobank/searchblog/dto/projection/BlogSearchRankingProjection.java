package com.kakaobank.searchblog.dto.projection;

import com.kakaobank.searchblog.dto.SearchRankResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface BlogSearchRankingProjection {
    Integer getRanking();
    String getKeyword();
    Integer getSearchCount();

    static SearchRankResponseDto convertToResponseRankingDto(BlogSearchRankingProjection blogSearchRankingProjection){
        return SearchRankResponseDto.builder()
                .keyword(blogSearchRankingProjection.getKeyword())
                .searchCount(blogSearchRankingProjection.getSearchCount())
                .build();
    }
}
