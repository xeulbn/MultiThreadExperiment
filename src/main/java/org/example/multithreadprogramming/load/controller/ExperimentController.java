package org.example.multithreadprogramming.load.controller;

import lombok.RequiredArgsConstructor;
import org.example.multithreadprogramming.load.service.*;
import org.example.multithreadprogramming.model.SiteResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequiredArgsConstructor
public class ExperimentController {
    private final LoadService loadService;
    private final RaceDemoService raceDemoService;
    private final MailboxDemoService mailboxDemoService;
    private final ProcIpcDemoService procIpcDemoService;
    private final MmapDemoService mmapDemoService;
    private final ThreadPoolConfigService threadPoolConfigService;

    // ===== CPU 비교 =====
    @GetMapping("/exp/cpu")
    public Map<String, Object> cpu(@RequestParam(defaultValue = "100") int tasks,
                                   @RequestParam(defaultValue = "100000") int rounds) {
        long sync = loadService.runCpuSync(tasks, rounds);
        long plat = loadService.runCpuPlatform(tasks, rounds);
        long virt = loadService.runCpuVirtual(tasks, rounds);
        return Map.of("tasks", tasks, "rounds", rounds,
                "sync_ms", sync, "platform_ms", plat, "virtual_ms", virt);
    }

    // ===== I/O 비교 (sleep로 블로킹 I/O 흉내) =====
    @GetMapping("/exp/io")
    public Map<String, Object> io(@RequestParam(defaultValue = "1000") int tasks,
                                  @RequestParam(defaultValue = "100") long ms) {
        long sync = loadService.runIoSync(tasks, ms);
        long plat = loadService.runIoPlatform(tasks, ms);
        long virt = loadService.runIoVirtual(tasks, ms);
        return Map.of("tasks", tasks, "sleep_ms_each", ms,
                "sync_ms", sync, "platform_ms", plat, "virtual_ms", virt);
    }

    // ===== 레이스 데모 =====
    @GetMapping("/exp/race")
    public Map<String, Object> race(@RequestParam(defaultValue = "16") int threads,
                                    @RequestParam(defaultValue = "100000") int iters) {
        long wrong = raceDemoService.incNoLock(threads, iters);
        long lock = raceDemoService.incWithLock(threads, iters);
        long atomic = raceDemoService.incAtomic(threads, iters);
        long expected = (long) threads * iters;
        return Map.of("threads", threads, "iters", iters,
                "expected", expected, "wrong_no_lock", wrong,
                "with_lock", lock, "atomic", atomic);
    }

    // ===== 메시지 패싱 (스레드) =====
    @GetMapping("/exp/mailbox")
    public Map<String, Object> mailbox(@RequestParam(defaultValue = "100000") int messages) throws Exception {
        long ms = mailboxDemoService.pingPong(messages);
        return Map.of("messages", messages, "elapsed_ms", ms, "type", "thread-BlockingQueue");
    }

    // ===== 메시지 패싱 (프로세스) =====
    @GetMapping("/exp/ipc-proc")
    public Map<String, Object> procIpc(@RequestParam(defaultValue = "10000") int messages) throws Exception {
        long ms = procIpcDemoService.pingPongProc(messages);
        return Map.of("messages", messages, "elapsed_ms", ms, "type", "process-stdin/stdout");
    }

    // ===== (유사) 공유 메모리: mmap =====
    @GetMapping("/exp/mmap")
    public Map<String, Object> mmap(@RequestParam(defaultValue = "10000000") int bytes) throws Exception {
        long ms = mmapDemoService.writeReadMmap(bytes);
        return Map.of("bytes", bytes, "elapsed_ms", ms);
    }

    // ===== 스레드풀 상태/리사이즈 =====
    @GetMapping("/pool/state")
    public Map<String, Object> poolState() {
        ThreadPoolTaskExecutor ex = threadPoolConfigService.getExecutor();
        ThreadPoolExecutor e = ex.getThreadPoolExecutor();
        return Map.of(
                "corePoolSize", e.getCorePoolSize(),
                "maxPoolSize", e.getMaximumPoolSize(),
                "poolSize", e.getPoolSize(),
                "activeCount", e.getActiveCount(),
                "queueSize", e.getQueue().size(),
                "completedTaskCount", e.getCompletedTaskCount()
        );
    }

    @PostMapping("/pool/resize")
    public Map<String, Object> resize(@RequestParam int core,
                                      @RequestParam int max) {
        threadPoolConfigService.reconfigure(core, max);
        return poolState();
    }

    // 데모용 핑
    @GetMapping("/ping")
    public Map<String, Object> ping(@RequestParam(defaultValue = "0") long ms) throws InterruptedException {
        if (ms > 0) Thread.sleep(ms);
        return Map.of("ok", true, "slept_ms", ms);
    }
}