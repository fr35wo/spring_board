package com.example.soulware_week1.global.dummy;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor(); //더미데이터를 전부 넣어야하니까 요청이 가득차도 받는 기본 큐 사용
        executor.setCorePoolSize(50); // 최소 스레드 수
        executor.setMaxPoolSize(130); // 최대 스레드 수
        executor.setQueueCapacity(1000); // 대기 큐 크기
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}

