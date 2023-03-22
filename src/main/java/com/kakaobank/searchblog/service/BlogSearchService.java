package com.kakaobank.searchblog.service;

import com.kakaobank.searchblog.dto.KakaoResponse;
import org.springframework.stereotype.Service;


@Service
public interface BlogSearchService {
    KakaoResponse getBlogsFromApi(String query, String sort, int page, int size);
}
