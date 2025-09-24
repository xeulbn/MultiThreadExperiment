package org.example.multithreadprogramming.load.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LoadService {
    private final ThreadPoolTaskExecutor platformExecutor;
    private final ExecutorService virtualPerTaskExecutor;

    // === CPU 바운드 작업 ===
    public long cpuWork(int rounds) {
        try {
            MessageDigest d = MessageDigest.getInstance("SHA-256");
            byte[] seed = new byte[32];
            for (int i = 0; i < rounds; i++) {
                d.update(seed);
                seed = d.digest();
            }
            return seed[0] & 0xFF;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // === I/O 바운드 흉내 (블로킹 sleep) ===
    public String ioSleep(long ms) {
        try {
            Thread.sleep(ms); // 가짜 Blocking I/O
            return "ok";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "interrupted";
        }
    }

    // ===== 동기 / 플랫폼 스레드 / 버추얼 스레드 비교 =====

    public long runCpuSync(int tasks, int rounds) {
        long t0 = System.nanoTime();
        for (int i = 0; i < tasks; i++) cpuWork(rounds);
        return (System.nanoTime() - t0) / 1_000_000;
    }

    public long runCpuPlatform(int tasks, int rounds) {
        long t0 = System.nanoTime();
        List<CompletableFuture<Long>> list = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            list.add(CompletableFuture.supplyAsync(() -> cpuWork(rounds), platformExecutor));
        }
        list.forEach(CompletableFuture::join);
        return (System.nanoTime() - t0) / 1_000_000;
    }

    public long runCpuVirtual(int tasks, int rounds) {
        long t0 = System.nanoTime();
        List<CompletableFuture<Long>> list = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            list.add(CompletableFuture.supplyAsync(() -> cpuWork(rounds), virtualPerTaskExecutor));
        }
        list.forEach(CompletableFuture::join);
        return (System.nanoTime() - t0) / 1_000_000;
    }

    public long runIoSync(int tasks, long ms) {
        long t0 = System.nanoTime();
        for (int i = 0; i < tasks; i++) ioSleep(ms);
        return (System.nanoTime() - t0) / 1_000_000;
    }

    public long runIoPlatform(int tasks, long ms) {
        long t0 = System.nanoTime();
        List<CompletableFuture<String>> list = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            list.add(CompletableFuture.supplyAsync(() -> ioSleep(ms), platformExecutor));
        }
        list.forEach(CompletableFuture::join);
        return (System.nanoTime() - t0) / 1_000_000;
    }

    public long runIoVirtual(int tasks, long ms) {
        long t0 = System.nanoTime();
        List<CompletableFuture<String>> list = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            list.add(CompletableFuture.supplyAsync(() -> ioSleep(ms), virtualPerTaskExecutor));
        }
        list.forEach(CompletableFuture::join);
        return (System.nanoTime() - t0) / 1_000_000;
    }
}
