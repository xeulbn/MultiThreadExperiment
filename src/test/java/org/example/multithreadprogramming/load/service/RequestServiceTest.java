package org.example.multithreadprogramming.load.service;

import org.example.multithreadprogramming.model.SiteResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    @Test
    void syncRequest() {
        RequestService requestService = new RequestService(); // 내부에서 RestTemplate new 함

        List<SiteResult> results = requestService.syncRequest();

        assertThat(results).hasSize(10);
        for (SiteResult result : results) {
            System.out.println(result.url() + " : " + result.timeMs() + " ms");
            assertThat(result.timeMs()).isGreaterThanOrEqualTo(0);
        }
    }
}