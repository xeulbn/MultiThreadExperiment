package org.example.multithreadprogramming.load.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ProcIpcDemoService {

    public long pingPongProc(int messages) throws Exception {
        String cp = System.getProperty("java.class.path");

        String childMain = "org.example.multithreadprogramming.load.service.PongChild";

        Process p = new ProcessBuilder(
                System.getProperty("java.home") + File.separator + "bin" + File.separator + "java",
                "-cp", cp,
                childMain
        ).redirectErrorStream(true).start();

        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(p.getOutputStream(), java.nio.charset.StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(p.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {

            long t0 = System.nanoTime();

            // 메시지 전송
            for (int i = 0; i < messages; i++) {
                out.write("X\n");
                out.flush();
                String echo = in.readLine();

            }
            out.write("END\n");
            out.flush();

            //END 보내고 곧바로 EOF를 보내 확실히 종료 유도
            out.close();
            // 자식의 첫 줄 응답 대기(OK 등)
            String line = in.readLine(); // 반드시 개행이 필요(println + flush)
            if (line == null) {
                // 디버깅 위해 잔여 출력 읽기
                StringBuilder sb = new StringBuilder();
                while (in.ready()) {
                    int ch = in.read();
                    if (ch == -1) break;
                    sb.append((char) ch);
                }
                throw new IllegalStateException("Child produced no line. Output: " + sb);
            }

            // 종료 대기 (타임아웃 보호)
            boolean exited = p.waitFor(10, java.util.concurrent.TimeUnit.SECONDS);
            if (!exited) {
                p.destroyForcibly();
                throw new IllegalStateException("Child did not exit within timeout");
            }

            return (System.nanoTime() - t0) / 1000000;
        }
    }
}
