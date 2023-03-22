package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.dto.KakaoResponse;
import com.kakaobank.searchblog.service.BlogSearchService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchServiceImpl_kakao_msa_001 implements BlogSearchService {
    private final BlogSearchServiceImpl_naver_msa_001 blogSearchServiceImplNaverMsa001;

    private WebClient webClient;

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();
    }

    @Override
    @HystrixCommand(fallbackMethod = "getBlogsFromNaverApi")
    public KakaoResponse getBlogsFromApi(String query, String sort, int page, int size){

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

    public KakaoResponse getBlogsFromNaverApi(String query, String sort, int page, int size, Throwable t) {
        log.error(ExceptionUtils.getStackTrace(t));
        return blogSearchServiceImplNaverMsa001.getBlogsFromApi(query, sort, page, size);
    }
}
