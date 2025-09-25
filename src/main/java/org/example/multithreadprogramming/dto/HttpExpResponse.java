package org.example.multithreadprogramming.dto;

import org.example.multithreadprogramming.model.SiteResult;

import java.util.List;

public record HttpExpResponse(String mode,
                              long total_ms,
                              List<SiteResult> results) {
}
