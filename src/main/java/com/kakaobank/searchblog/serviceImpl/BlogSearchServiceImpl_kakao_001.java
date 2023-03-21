package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.service.BlogSearchService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchServiceImpl_kakao_001 implements BlogSearchService {

    @Value("${api.kakao.authorization}")
    private String Authorization;
    @Value("${api.kakao.blog.reqUri}")
    private String ReqUri;
    private final RestTemplate restTemplate = new RestTemplate();
    private final BlogSearchServiceImpl_naver_001 blogSearchServiceImplNaver001;

    @Override
    @HystrixCommand(fallbackMethod = "getBlogsFromNaverApi")
    public Map getBlogsFromApi(String query, String sort, int page, int size){
        Map response   = null;

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(this.ReqUri)
                .queryParam("query", query)
                .queryParam("sort", sort)
                .queryParam("size", size)
                .queryParam("page", page)
                .encode(StandardCharsets.UTF_8)
                .build(false);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", this.Authorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<>(headers);

        response = restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, Map.class).getBody();
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("query", query);
        requestParam.put("sort", sort);
        requestParam.put("page", page);
        requestParam.put("size", size);
        requestParam.put("apiType", "kakao");
        response.put("requestParam", requestParam);

        return response;
    }

    public Map getBlogsFromNaverApi(String query, String sort, int page, int size, Throwable t) {
        log.error(ExceptionUtils.getStackTrace(t));
        return blogSearchServiceImplNaver001.getBlogsFromApi(query, sort, page, size);
    }
}
