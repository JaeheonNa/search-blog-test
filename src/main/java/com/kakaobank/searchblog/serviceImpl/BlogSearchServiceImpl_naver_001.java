package com.kakaobank.searchblog.serviceImpl;

import com.kakaobank.searchblog.service.BlogSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
public class BlogSearchServiceImpl_naver_001 implements BlogSearchService {

    @Value("${api.naver.blog.reqUri}")
    private String ReqUri;
    @Value("${api.naver.clientId}")
    private String clientId;
    @Value("${api.naver.clientSecret}")
    private String clientSecret;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map getBlogsFromApi(String query, String sort, int page, int size) {
        Map response   = null;

        if("accuracy".equals(sort)) sort = "sim";
        else sort = "date";

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(this.ReqUri)
                .queryParam("query", query)
                .queryParam("sort", sort)
                .queryParam("display", size)
                .queryParam("start", page)
                .encode(StandardCharsets.UTF_8)
                .build(false);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", this.clientId);
        headers.add("X-Naver-Client-Secret" ,this.clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity<>(headers);

        response = restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, Map.class).getBody();
        makeKakaoResponseFormat(response);
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("query", query);
        requestParam.put("sort", sort);
        requestParam.put("page", page);
        requestParam.put("size", size);
        requestParam.put("apiType", "naver");
        response.put("requestParam", requestParam);

        return response;
    }

    private void makeKakaoResponseFormat(Map response) {
        List<Map<String, Object>> documents = (List) response.get("items");
        response.put("documents", documents);
        for(Map document : documents){
            document.put("url", document.get("link"));
            document.put("contents", document.get("description"));
            document.put("blogname", document.get("bloggername"));
            document.put("thumbnail", null);
            document.put("datetime", document.get("postdate"));
            document.remove("link");
            document.remove("description");
            document.remove("bloggername");
            document.remove("bloggerlink");
            document.remove("postdate");
        }
        Map<String, Object> meta = new HashMap<>();
        meta.put("total_count", response.get("total"));
        meta.put("is_end", (Integer)response.get("start")*(Integer)response.get("display") >= (Integer)response.get("total"));
        meta.put("pageable_count", null);
        response.put("meta", meta);

        response.remove("items");
        response.remove("lastBuildDate");
        response.remove("start");
        response.remove("display");
        response.remove("total");
    }
}
