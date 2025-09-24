package org.example.multithreadprogramming.load.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class MetricsService {


    private final MeterRegistry registry;

    public <T> T time(String name, Supplier<T> sup) {
        long t0 = System.nanoTime();
        T v = sup.get();
        long ms = (System.nanoTime() - t0) / 1_000_000;
        registry.timer(name).record(ms, java.util.concurrent.TimeUnit.MILLISECONDS);
        return v;
    }

    public void gaugeThreadPool(ThreadPoolTaskExecutor ex) {
        ThreadPoolExecutor e = ex.getThreadPoolExecutor();
        Gauge.builder("pool.queue.size", e, p -> p.getQueue().size()).register(registry);
        Gauge.builder("pool.active", e, ThreadPoolExecutor::getActiveCount).register(registry);
        Gauge.builder("pool.poolsize", e, ThreadPoolExecutor::getPoolSize).register(registry);
        Gauge.builder("pool.completed", e, p -> p.getCompletedTaskCount()).register(registry);
    }
}
