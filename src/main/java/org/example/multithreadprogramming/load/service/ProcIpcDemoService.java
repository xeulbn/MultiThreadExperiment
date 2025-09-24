package org.example.multithreadprogramming.load.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ProcIpcDemoService {

    public long pingPongProc(int messages) throws Exception {
        // 현재 클래스패스 재사용
        String cp = System.getProperty("java.class.path");
        Process p = new ProcessBuilder(
                System.getProperty("java.home") + File.separator + "bin" + File.separator + "java",
                "-cp", cp,
                "com.example.multithread.demo.PongChild"
        ).redirectErrorStream(true).start();

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

        long t0 = System.nanoTime();
        for (int i = 0; i < messages; i++) {
            out.write("X\n");
        }
        out.write("END\n");
        out.flush();

        String line = in.readLine(); // 자식이 받은 개수 회신
        p.waitFor();

        return (System.nanoTime() - t0) / 1_000_000;
    }
}