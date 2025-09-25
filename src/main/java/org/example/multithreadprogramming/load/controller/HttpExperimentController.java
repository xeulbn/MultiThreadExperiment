package org.example.multithreadprogramming.load.controller;

import lombok.RequiredArgsConstructor;
import org.example.multithreadprogramming.dto.HttpExpResponse;
import org.example.multithreadprogramming.load.service.RequestService;
import org.example.multithreadprogramming.model.SiteResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exp/http")
public class HttpExperimentController {

    private final RequestService requestService;

    @GetMapping("/sync")
    public HttpExpResponse httpSync() {
        long t0 = System.currentTimeMillis();
        List<SiteResult> res = requestService.syncRequest();
        long ms = System.currentTimeMillis() - t0;
        return new HttpExpResponse("sync", ms, res);
    }

    @GetMapping("/async")
    public HttpExpResponse httpAsync() {
        long t0 = System.currentTimeMillis();
        List<SiteResult> res = requestService.asyncRequest();
        long ms = System.currentTimeMillis() - t0;
        return new HttpExpResponse("async", ms, res);
    }


}
