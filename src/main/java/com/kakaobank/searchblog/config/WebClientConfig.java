package com.kakaobank.searchblog.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Value("${api.kakao.blog.baseUri}")
    private String kakaoBaseUrl;
    @Value("${api.naver.blog.baseUri}")
    private String naverBaseUrl;

    @Bean
    @Qualifier("kakaoWebClient")
    public WebClient kakaoWebClient(){
        return WebClient.builder()
                .baseUrl(this.kakaoBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();
    }
    @Bean
    @Qualifier("naverWebClient")
    public WebClient naverWebClient(){
        return WebClient.builder()
                .baseUrl(this.naverBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();
    }
}
