package com.kakaobank.searchblog.controller;

import com.kakaobank.searchblog.dto.SearchRankResponseDto;
import com.kakaobank.searchblog.dto.projection.BlogSearchRankingProjection;
import com.kakaobank.searchblog.service.BlogSearchService;
import com.kakaobank.searchblog.service.SearchRankingService;
import com.kakaobank.searchblog.serviceFactory.BlogSearchServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("search/blog")
public class BlogSearchController {

    private final BlogSearchServiceFactory blogSearchServiceFactory;

    private static final Map<String, Object> combo = new HashMap<>(){{
        put("SortCombo", new ArrayList<>(){{ add("accuracy"); add("recency"); }});
        put("SizeCombo", new ArrayList<>(){{ add(10); add(20); add(30); add(40); add(50); }});
    }};

    @GetMapping()
    public Map getBlogByKeywordFromKakao(@RequestParam(name = "query") String query
                                 , @RequestParam(name = "sort", required = false, defaultValue = "accuracy") String sort
                                 , @RequestParam(name = "page", required = false, defaultValue = "1") int page
                                 , @RequestParam(name = "size", required = false, defaultValue = "10") int size
                                 , @RequestParam(name = "apiType", required = false, defaultValue = "kakao") String apiType) {
        Map<String, Object> response = new HashMap<>();
        /* 특정 API를 호출하는 Service 객체를 가져온다. */
        BlogSearchService blogSearchService = blogSearchServiceFactory.getBlogSearchService(apiType);
        /* Service 객체를 통해 API를 조회한다. */
        Map blogSearchResult = blogSearchService.getBlogsFromApi(query, sort, page, size);
        /* 랭킹을 관리하는 Service 객체를 가져온다. */
        SearchRankingService searchRankingService = blogSearchServiceFactory.getSearchRankingService();
        /* 만약 1페이지로 검색 요청이 들어오면 검색어를 랭킹 정보 조회용 DB에 저장한다. */

        if(page == 1) searchRankingService.insertSearchRankingToRedis(query);
        List<SearchRankResponseDto> searchRankingResult = searchRankingService.getSearchRankingFromRedis();

//
//
//            if(page == 1) searchRankingService.insertSearchRanking(query);
//            /* 인기 검색어 랭킹 정보를 조회한다. */
//            List<BlogSearchRankingProjection> searchRankingResult = searchRankingService.getSearchRanking();
//            /* 조회 결과를 Map에 담아 return 한다. */
//
//
        response.put("blogSearchResult", blogSearchResult);
        response.put("searchRankingResult", searchRankingResult);
        return response;
    }

    @GetMapping("combo")
    public Map getSortingCombo(){
        return this.combo;
    }
}
