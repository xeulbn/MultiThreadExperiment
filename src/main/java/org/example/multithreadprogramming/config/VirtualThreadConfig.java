package org.example.multithreadprogramming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadConfig {
    @Bean(destroyMethod = "close")
    public ExecutorService virtualPerTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor(); // JDK 21
    }
}
