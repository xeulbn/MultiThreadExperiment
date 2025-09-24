package org.example.multithreadprogramming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "platformExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setThreadNamePrefix("plat-");
        ex.setCorePoolSize(4);          // 데모용 기본값
        ex.setMaxPoolSize(16);
        ex.setQueueCapacity(200);       // 유한 큐 → 포화/백프레셔 체험
        ex.setKeepAliveSeconds(60);
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 백프레셔
        ex.initialize();
        return ex;
    }
}
