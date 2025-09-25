package org.example.multithreadprogramming.load.service;


import lombok.RequiredArgsConstructor;
import org.example.multithreadprogramming.model.SiteResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class HttpAsyncClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Async("platformExecutor")
    public CompletableFuture<SiteResult> callSiteAsync(String url) {
        long start = System.currentTimeMillis();
        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception ignored) { }
        long end = System.currentTimeMillis();
        return CompletableFuture.completedFuture(new SiteResult(url, end - start));
    }
}
