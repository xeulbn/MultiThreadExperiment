package org.example.multithreadprogramming.dto;

public record TimingResponse(long sync_ms, long platform_ms, long virtual_ms) {
}
