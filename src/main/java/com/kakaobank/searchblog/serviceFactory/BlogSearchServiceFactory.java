package com.kakaobank.searchblog.serviceFactory;

import com.kakaobank.searchblog.service.BlogSearchService;
import com.kakaobank.searchblog.service.SearchRankingService;
import com.kakaobank.searchblog.serviceImpl.BlogSearchServiceImpl_kakao_001;
import com.kakaobank.searchblog.serviceImpl.BlogSearchServiceImpl_naver_001;
import com.kakaobank.searchblog.serviceImpl.SearchRankingServiceImpl_001;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogSearchServiceFactory {
    private final BlogSearchServiceImpl_kakao_001 blogSearchServiceImpl_kakao_001;
    private final BlogSearchServiceImpl_naver_001 blogSearchServiceImpl_naver_001;
    private final SearchRankingServiceImpl_001 SearchRankingServiceImpl_001;

    public BlogSearchService getBlogSearchService(String apiType){
        if("naver".equals(apiType)) return blogSearchServiceImpl_naver_001;
        else return blogSearchServiceImpl_kakao_001;
    }

    public SearchRankingService getSearchRankingService(){
        return SearchRankingServiceImpl_001;
    }


}
