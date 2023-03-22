package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.service.BlogSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchServiceImpl_naver_msa_001 implements BlogSearchService {

    @Value("${api.naver.blog.reqUri}")
    private String ReqUri;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map getBlogsFromApi(String query, String sort, int page, int size) {
        Map response = null;

        if ("accuracy".equals(sort)) sort = "sim";
        else sort = "date";

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(this.ReqUri)
                .queryParam("query", query)
                .queryParam("sort", sort)
                .queryParam("display", size)
                .queryParam("start", page)
                .encode(StandardCharsets.UTF_8)
                .build(false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<>(headers);

        response = restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, Map.class).getBody();
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("query", query);
        requestParam.put("sort", sort);
        requestParam.put("page", page);
        requestParam.put("size", size);
        requestParam.put("apiType", "naver");
        response.put("requestParam", requestParam);

        return response;
    }
}