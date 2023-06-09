package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.dto.KakaoResponse;
import com.kakaobank.searchblog.service.BlogSearchService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchServiceImpl_naver_msa_001 implements BlogSearchService {

    // Qualifier로 구분하는 빈은 생성자 주입 방식으로 DI되지 않음. (왜일까)
    // 반드시 Autowired를 써야 함.
    @Autowired
    @Qualifier("naverWebClient")
    private WebClient webClient;

    @Override
    public KakaoResponse getBlogsFromApi(String query, String sort, int page, int size) {
        Mono<KakaoResponse> kakaoResponseMono = webClient.get().uri(uriBuilder1 ->
                        uriBuilder1.path("/search/blog")
                                .queryParam("query", query)
                                .queryParam("sort", sort)
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .build()
                ).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(KakaoResponse.class);
        return kakaoResponseMono.share().block();
    }
}