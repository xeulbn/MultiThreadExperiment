package org.example.multithreadprogramming.load.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Service
public class RaceDemoService {
    private long counter = 0L;
    private final Object lock = new Object();

    public long incNoLock(int threads, int iters) {
        counter = 0;
        IntStream.range(0, threads).parallel().forEach(t -> {
            for (int i = 0; i < iters; i++) {
                counter++; // 레이스!
            }
        });
        return counter;
    }

    public long incWithLock(int threads, int iters) {
        counter = 0;
        IntStream.range(0, threads).parallel().forEach(t -> {
            for (int i = 0; i < iters; i++) {
                synchronized (lock) {
                    counter++;
                }
            }
        });
        return counter;
    }

    public long incAtomic(int threads, int iters) {
        AtomicLong a = new AtomicLong(0);
        IntStream.range(0, threads).parallel().forEach(t -> {
            for (int i = 0; i < iters; i++) a.incrementAndGet();
        });
        return a.get();
    }
}