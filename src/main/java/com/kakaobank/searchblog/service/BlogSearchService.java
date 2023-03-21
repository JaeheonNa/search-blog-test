package com.kakaobank.searchblog.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface BlogSearchService {
    Map getBlogsFromApi(String query, String sort, int page, int size);
}
