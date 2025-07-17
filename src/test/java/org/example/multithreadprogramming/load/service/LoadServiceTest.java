package org.example.multithreadprogramming.load.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {

    LoadService loadService = new LoadService();

    @Test
    void asyncHeavyTask() {
        loadService.syncHeavyTask(1);
    }

    @Test
    void syncHeavyTask() throws Exception{
        CompletableFuture.runAsync(() -> loadService.syncHeavyTask(1));
        TimeUnit.SECONDS.sleep(1);
    }
}