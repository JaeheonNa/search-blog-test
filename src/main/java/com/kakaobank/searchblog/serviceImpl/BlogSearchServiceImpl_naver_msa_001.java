package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.dto.KakaoResponse;
import com.kakaobank.searchblog.service.BlogSearchService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchServiceImpl_naver_msa_001 implements BlogSearchService {


    private WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();
    }
    @Override
    public KakaoResponse getBlogsFromApi(String query, String sort, int page, int size) {
        KakaoResponse response = null;

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

        KakaoResponse kakaoResponse = kakaoResponseMono.share().block();

        return kakaoResponse;
    }
}