package org.example.multithreadprogramming.load.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

@Service
@RequiredArgsConstructor
public class ThreadPoolConfigService {
    private final ThreadPoolTaskExecutor taskExecutor;

    public ThreadPoolTaskExecutor getExecutor() {
        return taskExecutor;
    }

    public void reconfigure(int core, int max) {
        ThreadPoolExecutor e = taskExecutor.getThreadPoolExecutor();
        e.setCorePoolSize(core);
        e.setMaximumPoolSize(Math.max(core, max));
    }
}
