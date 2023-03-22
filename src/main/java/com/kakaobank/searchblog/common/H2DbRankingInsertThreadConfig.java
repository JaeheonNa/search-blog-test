package com.kakaobank.searchblog.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class H2DbRankingInsertThreadConfig {

    @Bean
    public TaskExecutor h2DbRankingInsertThreadExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(3);
        executor.setThreadNamePrefix("H2_DB_Thread_");
        executor.initialize();
        return executor;
    }

}
