package org.example.multithreadprogramming.dto;

public record PoolState(int corePoolSize, int maxPoolSize, int poolSize, int activeCount,
                        int queueSize, long completedTaskCount) {
}
