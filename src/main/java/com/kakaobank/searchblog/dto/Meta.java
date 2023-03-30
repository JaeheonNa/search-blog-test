package com.kakaobank.searchblog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Meta {

    int total_count;
    int pageable_count;

    // JsonProperty 어노테이션을 달아줘야 'is'가 생략되지 않고 출력됨.
    // Wrapper 타입으로 선언해줘야 중복 맵핑/출력 방지할 수 있음.
    @JsonProperty(value = "is_end")
    Boolean is_end;
}
