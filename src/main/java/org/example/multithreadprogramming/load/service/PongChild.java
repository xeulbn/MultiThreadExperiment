package org.example.multithreadprogramming.load.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PongChild {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, UTF_8));
        String s;
        // END를 받으면 EOF를 기다리지 않고 즉시 탈출
        while ((s = br.readLine()) != null) {
            if ("END".equals(s))
                break; // END 받으면 끝
            System.out.println(s);
            System.out.flush();
        }
        // 부모가 readLine()으로 한 줄을 기다리므로 개행 포함 + flush 보장
        System.out.println("OK");
        System.out.flush();
    }
}