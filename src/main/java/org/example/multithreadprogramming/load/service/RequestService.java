package org.example.multithreadprogramming.load.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.multithreadprogramming.model.SiteResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {

    private final List<String> urls=List.of(
            "https://www.google.com", "https://www.naver.com",
            "https://toss.im", "https://www.wikipedia.org",
            "https://www.stackoverflow.com", "https://www.github.com",
            "https://www.microsoft.com", "https://www.apple.com",
            "https://www.youtube.com", "https://www.netflix.com"
    );

    private final HttpAsyncClient httpAsyncClient;

    private final RestTemplate restTemplate= new RestTemplate();

    public List<SiteResult> syncRequest(){
        List<SiteResult> results=new ArrayList<>();
        for(String url : urls){
            results.add(callSite(url));
        }
        return results;
    }

    public List<SiteResult> asyncRequest() {
        List<CompletableFuture<SiteResult>> futures = new ArrayList<>();
        for (String url : urls) {
            futures.add(httpAsyncClient.callSiteAsync(url));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<SiteResult> results = new ArrayList<>();
        for (CompletableFuture<SiteResult> f : futures) {
            results.add(f.join());
        }
        return results;
    }

    private SiteResult callSite(String url) {
        long start = System.currentTimeMillis();
        try {
            restTemplate.getForObject(url, String.class);
        } catch (Exception ignored) {

        }
        long end = System.currentTimeMillis();
        return new SiteResult(url, end - start);
    }
}
