package org.example.multithreadprogramming.load.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class MmapDemoService {
    public long writeReadMmap(int bytes) throws IOException {
        Path path = Files.createTempFile("mmap", ".bin");
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            ch.truncate(bytes);
            MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_WRITE, 0, bytes);
            long t0 = System.nanoTime();
            for (int i = 0; i < bytes; i++) buf.put((byte) 1);
            buf.force();
            long ms = (System.nanoTime() - t0) / 1_000_000;
            return ms;
        } finally {
            Files.deleteIfExists(path);
        }
    }
}