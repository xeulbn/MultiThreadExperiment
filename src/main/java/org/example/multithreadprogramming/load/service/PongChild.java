package org.example.multithreadprogramming.load.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PongChild {
    public static void main(String[] args) throws Exception {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String s;
            while ((s = br.readLine()) != null) {
                if ("END".equals(s)) break;
                count++;
            }
        }
        System.out.println(count);
    }
}