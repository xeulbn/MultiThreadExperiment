package org.example.multithreadprogramming.load.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "metrics";
    }
}