package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.dto.KakaoResponse;
import com.kakaobank.searchblog.service.BlogSearchService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchServiceImpl_kakao_msa_001 implements BlogSearchService {
    private final BlogSearchServiceImpl_naver_msa_001 blogSearchServiceImplNaverMsa001;

    // Qualifier로 구분하는 빈은 생성자 주입 방식으로 DI되지 않음. (왜일까)
    // 반드시 Autowired를 써야 함.
    @Autowired
    @Qualifier("kakaoWebClient")
    private WebClient webClient;

    @Override
    @HystrixCommand(
            fallbackMethod = "getBlogsFromNaverApi",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")})
    public KakaoResponse getBlogsFromApi(String query, String sort, int page, int size){

        log.info("시작");
        Mono<KakaoResponse> kakaoResponseMono = webClient.get().uri(uriBuilder1 ->
                        uriBuilder1.path("/search/blog")
                                .queryParam("query", query)
                                .queryParam("sort", sort)
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .build()
                ).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(KakaoResponse.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(3)));

        return kakaoResponseMono.block();
    }

    public KakaoResponse getBlogsFromNaverApi(String query, String sort, int page, int size, Throwable t) {
        log.error(ExceptionUtils.getStackTrace(t));
        return blogSearchServiceImplNaverMsa001.getBlogsFromApi(query, sort, page, size);
    }
}
