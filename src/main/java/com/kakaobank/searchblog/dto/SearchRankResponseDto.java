package com.kakaobank.searchblog.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Objects;

@Data
@Builder
public class SearchRankResponseDto {
    String keyword;
    Double count;

    public static SearchRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple<String> rankInfo){
        if (Objects.isNull(rankInfo)) {
            return null;
        }
        return SearchRankResponseDto.builder()
                .keyword(rankInfo.getValue())
                .count(rankInfo.getScore())
                .build();
    }
}
