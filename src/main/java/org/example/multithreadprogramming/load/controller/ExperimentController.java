package org.example.multithreadprogramming.load.controller;

import lombok.RequiredArgsConstructor;
import org.example.multithreadprogramming.dto.MsResponse;
import org.example.multithreadprogramming.dto.PoolState;
import org.example.multithreadprogramming.dto.TimingResponse;
import org.example.multithreadprogramming.load.service.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;



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

    // ===== 스레드풀 상태/리사이즈 =====
    @GetMapping("/pool/state")
    public PoolState poolState() {
        ThreadPoolTaskExecutor ex = threadPoolConfigService.getExecutor();
        ThreadPoolExecutor e = ex.getThreadPoolExecutor();
        return new PoolState(
                e.getCorePoolSize(),
                e.getMaximumPoolSize(),
                e.getPoolSize(),
                e.getActiveCount(),
                e.getQueue().size(),
                e.getCompletedTaskCount()
        );
    }

    @PostMapping("/pool/resize")
    public PoolState resize(@RequestParam int core,
                            @RequestParam int max) {
        threadPoolConfigService.reconfigure(core, max);
        return poolState();
    }

    // ===== CPU 비교 =====
    @GetMapping( "/exp/cpu")
    public TimingResponse cpu(@RequestParam(defaultValue = "100") int tasks,
                              @RequestParam(defaultValue = "100000") int rounds) {
        long sync = loadService.runCpuSync(tasks, rounds);
        long plat = loadService.runCpuPlatform(tasks, rounds);
        long virt = loadService.runCpuVirtual(tasks, rounds);
        return new TimingResponse(sync, plat, virt);
    }

    // ===== I/O 비교 (sleep로 블로킹 I/O 흉내) =====
    @GetMapping("/exp/io")
    public TimingResponse io(@RequestParam(defaultValue = "1000") int tasks,
                                  @RequestParam(defaultValue = "100") long ms) {
        long sync = loadService.runIoSync(tasks, ms);
        long plat = loadService.runIoPlatform(tasks, ms);
        long virt = loadService.runIoVirtual(tasks, ms);
        return new TimingResponse(sync,plat,virt);
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
    public MsResponse mailbox(@RequestParam(defaultValue = "100000") int messages) throws Exception {
        long ms = mailboxDemoService.pingPong(messages);
        return new MsResponse(ms);
    }

    // ===== 메시지 패싱 (프로세스) =====
    @GetMapping("/exp/proc")
    public MsResponse procIpc(@RequestParam(defaultValue = "10000") int messages) throws Exception {
        long ms = procIpcDemoService.pingPongProc(messages);
        return new MsResponse(ms);
    }

    // ===== 공유 메모리 nmap=====
    @GetMapping("/exp/mmap")
    public MsResponse mmap(@RequestParam(defaultValue = "10000000") int bytes) throws Exception {
        long ms = mmapDemoService.writeReadMmap(bytes);
        return new MsResponse(ms);
    }

    // 데모용 핑
    @GetMapping("/ping")
    public Map<String, Object> ping(@RequestParam(defaultValue = "0") long ms) throws InterruptedException {
        if (ms > 0) Thread.sleep(ms);
        return Map.of("ok", true, "slept_ms", ms);
    }
}