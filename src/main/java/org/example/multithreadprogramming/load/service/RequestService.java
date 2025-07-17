package org.example.multithreadprogramming.load.service;

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
public class RequestService {

    private final List<String> urls=List.of(
            "https://www.google.com", "https://www.naver.com",
            "https://toss.im", "https://www.wikipedia.org",
            "https://www.stackoverflow.com", "https://www.github.com",
            "https://www.microsoft.com", "https://www.apple.com",
            "https://www.youtube.com", "https://www.netflix.com"
    );

    private final RestTemplate restTemplate= new RestTemplate();

    public List<SiteResult> syncRequest(){
        List<SiteResult> results=new ArrayList<>();
        for(String url : urls){
            results.add(callSite(url));
        }
        return results;
    }

    public List<SiteResult> asyncRequest(){
        List<CompletableFuture<SiteResult>> futures = new ArrayList<>();
        for(String url : urls){
            futures.add(callSiteAsync(url));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        List<SiteResult> results=new ArrayList<>();

        for(CompletableFuture<SiteResult> future : futures){
            try{
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error during async call",e);
            }
        }
        return results;
    }

    private SiteResult callSite (String url){
        long start= System.currentTimeMillis();
        try{
            restTemplate.getForObject(url,String.class);
        }catch(Exception e){
            log.warn("Request failed : {}",url);
        }
        long end = System.currentTimeMillis();
        return new SiteResult(url,end-start);
    }

    @Async
    public CompletableFuture<SiteResult> callSiteAsync (String url){
        return CompletableFuture.completedFuture(callSite(url));
    }
}
