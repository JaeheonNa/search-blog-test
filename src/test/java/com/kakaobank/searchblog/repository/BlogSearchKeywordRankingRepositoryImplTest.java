package com.kakaobank.searchblog.repository;

import com.kakaobank.searchblog.dto.entity.BlogSearchKeywordRanking;
import com.kakaobank.searchblog.dto.entity.QBlogSearchKeywordRanking;
import com.kakaobank.searchblog.dto.projection.BlogSearchRankingProjection;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
class BlogSearchKeywordRankingRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    public BlogSearchKeywordRankingRepositoryImplTest(){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public List<BlogSearchRankingProjection> findRankingList() {
        BlogSearchKeywordRanking entity1 = new BlogSearchKeywordRanking();
        em.persist(entity1);

        QBlogSearchKeywordRanking entity = QBlogSearchKeywordRanking.blogSearchKeywordRanking;

        LocalDateTime beforeOneDay = LocalDateTime.now().minusHours(24);
        List<BlogSearchKeywordRanking> tuple = queryFactory.select(entity)
                .from(entity)
                .where(entity.create_dtime.gt(beforeOneDay))
                .groupBy(entity.keyword)
                .fetch();

        System.out.println(tuple);

        Assertions.assertThat(entity1).isEqualTo(tuple.get(0));

        return null;
    }
}