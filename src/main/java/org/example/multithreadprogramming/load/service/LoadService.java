package org.example.multithreadprogramming.load.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class LoadService {

    @Async
    public void asyncHeavyTask(int taskId) {
        heavyComputation(taskId,"[Async]");
    }

    public void syncHeavyTask(int taskId){
        heavyComputation(taskId,"[Sync]");
    }

    private void heavyComputation(int taskId, String tag) {
        long start=System.currentTimeMillis();
        IntStream.range(0,1_000_000).map(i->i*i).sum();

        long end=System.currentTimeMillis();
        System.out.println(tag+"Task "+taskId+" finished in  "+(end-start)+" ms");
    }


}
