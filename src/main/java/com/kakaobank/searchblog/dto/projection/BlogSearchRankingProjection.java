package com.kakaobank.searchblog.dto.projection;

import org.springframework.stereotype.Service;

@Service
public interface BlogSearchRankingProjection {
    Integer getRanking();
    String getKeyword();
    Integer getSearchCount();
}
