package com.kakaobank.searchblog.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Objects;

@Data
@Builder
public class SearchRankResponseDto {
    private String keyword;
    private int searchCount;
    private int rank;

    public static SearchRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple<String> rankInfo){
        if (Objects.isNull(rankInfo)) return null;
        int count = rankInfo.getScore().intValue();

        return SearchRankResponseDto.builder()
                .keyword(rankInfo.getValue())
                .searchCount(count)
                .build();
    }
}
