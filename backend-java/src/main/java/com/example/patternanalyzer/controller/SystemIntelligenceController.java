package com.example.patternanalyzer.controller;

import com.example.patternanalyzer.domain.dto.PlatformDtos;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class SystemIntelligenceController {
    private final PlatformController platform;

    public SystemIntelligenceController(PlatformController platform) { this.platform = platform; }

    @GetMapping("/system/status")
    Map<String, Object> systemStatus() { return platform.systemStatus(); }
    @GetMapping("/system/dashboard")
    PlatformDtos.SystemDashboard systemDashboard() { return platform.systemDashboard(); }
    @GetMapping("/system/timeline")
    List<PlatformDtos.SystemMetric> systemTimeline() { return platform.systemTimeline(); }
    @GetMapping("/system/alerts")
    List<PlatformDtos.SystemAlert> systemAlerts() { return platform.systemAlerts(); }
    @GetMapping("/system/logs")
    List<PlatformDtos.SystemLog> systemLogs() { return platform.systemLogs(); }
    @GetMapping("/system/jobs")
    List<PlatformDtos.BackgroundJob> backgroundJobs() { return platform.backgroundJobs(); }

    @GetMapping("/intelligence/news")
    List<PlatformDtos.NewsItem> news(@RequestParam(required = false) String query) { return platform.news(query); }
    @GetMapping("/intelligence/sentiment")
    PlatformDtos.SentimentSummary sentiment() { return platform.sentiment(); }
    @GetMapping("/intelligence/calendar")
    List<PlatformDtos.MarketEvent> calendar() { return platform.calendar(); }
}
