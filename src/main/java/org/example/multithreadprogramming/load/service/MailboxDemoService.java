package org.example.multithreadprogramming.load.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

@Service
public class MailboxDemoService {
    // 스레드 간 메시지 패싱 (BlockingQueue)
    public long pingPong(int messages) throws Exception {
        ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<>(1024);

        Thread pong = Thread.startVirtualThread(() -> {
            try {
                while (true) {
                    Integer v = q.take();
                    if (v == -1) break;
                }
            } catch (InterruptedException ignored) {}
        });

        long t0 = System.nanoTime();
        for (int i = 0; i < messages; i++) q.put(i);
        q.put(-1);
        pong.join();
        return (System.nanoTime() - t0) / 1_000_000;
    }
}
