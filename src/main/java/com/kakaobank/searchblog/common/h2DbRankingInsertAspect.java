package com.kakaobank.searchblog.common;


import com.kakaobank.searchblog.serviceImpl.InsertRankingServiceImpl_001;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class h2DbRankingInsertAspect {

    @Qualifier("h2DbRankingInsertThreadExecutor")
    @Autowired
    private TaskExecutor h2DbRanking_Insert_Thread;

    private final InsertRankingServiceImpl_001 insertRankingServiceImpl_001;

    @Around("@annotation(H2DbRankingInsert)")
    public Map mongoDBLogger(ProceedingJoinPoint joinPoint) throws Throwable{
        String query = (String) joinPoint.getArgs()[0];
        if(query != null || !query.isBlank() || !query.equals("") || !query.isEmpty() ) {
            insertRankingServiceImpl_001.setQuery(query);
            h2DbRanking_Insert_Thread.execute(insertRankingServiceImpl_001);
        }
        Map response = (Map) joinPoint.proceed();
        return response;
    }
}