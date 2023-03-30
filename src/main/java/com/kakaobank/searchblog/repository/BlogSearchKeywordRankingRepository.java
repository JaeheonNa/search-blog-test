package com.kakaobank.searchblog.repository;

import com.kakaobank.searchblog.dto.entity.BlogSearchKeywordRanking;
import com.kakaobank.searchblog.dto.projection.BlogSearchRankingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface BlogSearchKeywordRankingRepository extends JpaRepository<BlogSearchKeywordRanking, Long> {

    @Query(value = "select dense_rank() over(order by a.searchCount desc) as ranking, " +
            "              a.keyword, " +
            "              a.searchCount " +
            "       from (select count(*) as searchCount," +
            "                    keyword" +
            "             from blog_search_keyword_ranking " +
            "             where create_dtime > timestampadd(hour, -24, now()) " +
            "             group by keyword) a " +
            "       order by ranking " +
            "       limit 10", nativeQuery = true)
    List<BlogSearchRankingProjection> findRankingList();


}