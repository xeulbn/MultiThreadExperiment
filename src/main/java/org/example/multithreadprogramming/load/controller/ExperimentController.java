package org.example.multithreadprogramming.load.controller;

import lombok.RequiredArgsConstructor;
import org.example.multithreadprogramming.load.service.LoadService;
import org.example.multithreadprogramming.load.service.MetricsService;
import org.example.multithreadprogramming.load.service.RequestService;
import org.example.multithreadprogramming.load.service.ThreadPoolConfigService;
import org.example.multithreadprogramming.model.SiteResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ExperimentController {

    private final LoadService loadService;
    private final RequestService requestService;
    private final ThreadPoolConfigService threadPoolService;
    private final MetricsService metricsService;

    @GetMapping("/cpu/async")
    public String runAsyncCpuTasks(){
        for(int i=0;i<20;i++){
            loadService.asyncHeavyTask(i);
        }

        return "Started 20 async CPU-bound tasks!";
    }

    @GetMapping("/cpu/sync")
    public String runSyncCpuTasks(){
        for(int i=0;i<20;i++){
            loadService.syncHeavyTask(i);
        }
        return "Started 20 async CPU-bound tasks!";
    }

    @GetMapping("/io/async")
    public List<SiteResult> asyncRequest(){
        return requestService.asyncRequest();
    }

    @GetMapping("/io/sync")
    public List<SiteResult> syncRequest(){
        return requestService.syncRequest();
    }

    @PostMapping("/threadpool/config")
    public String updateThreadPool(@RequestBody Map<String,Integer> config){
        int core=config.getOrDefault("core",4);
        int maximum=config.getOrDefault("maximum",8);
        int queue= config.getOrDefault("queue",100);
        threadPoolService.updatePoolSettings(core,maximum,queue);

        return String.format("Updated thread pool : core=%d, maximum=%d, queue=%d", core, maximum, queue);
    }

    @GetMapping("/metrics")
    public Map<String,Object> getMetrics(){
        return metricsService.collectMetrics();
    }
}