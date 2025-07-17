package org.example.multithreadprogramming.load.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

@Service
public class MetricsService {

    private final ThreadPoolTaskExecutor executor;

    public MetricsService(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    public Map<String, Object> collectMetrics(){
        Map<String, Object> metrics = new HashMap<>();

        metrics.put("activeThreads", executor.getActiveCount());
        metrics.put("corePoolSize", executor.getCorePoolSize());
        metrics.put("maxPoolSize",executor.getMaxPoolSize());
        metrics.put("poolSize",executor.getPoolSize());
        metrics.put("queueSize",executor.getThreadPoolExecutor().getQueue().size());

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        metrics.put("availableProcessors",osBean.getAvailableProcessors());

        MemoryMXBean memoryBean= ManagementFactory.getMemoryMXBean();
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
        metrics.put("heapMemoryUsedMB", heapUsed/(1024*1024));

        return metrics;
    }

}
