package com.example.patternanalyzer.controller;

import com.example.patternanalyzer.domain.dto.PlatformDtos;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class AnalyticsSettingsController {
    private final PlatformController platform;

    public AnalyticsSettingsController(PlatformController platform) { this.platform = platform; }

    @GetMapping("/analytics/overview")
    PlatformDtos.AnalyticsOverview analyticsOverview(@RequestParam(defaultValue = "30D") String range) { return platform.analyticsOverview(range); }
    @GetMapping("/analytics/trades")
    List<PlatformDtos.AnalyticsTrade> analyticsTrades() { return platform.analyticsTrades(); }
    @GetMapping("/analytics/logs")
    List<PlatformDtos.AnalyticsLog> analyticsLogs() { return platform.analyticsLogs(); }

    @GetMapping("/settings")
    PlatformDtos.Settings settings() { return platform.settings(); }
    @PutMapping("/settings")
    PlatformDtos.Settings updateSettings(@Valid @RequestBody PlatformDtos.Settings settings) { return platform.updateSettings(settings); }
    @PostMapping("/settings/backup")
    Map<String, Object> backup() { return platform.backup(); }
    @PostMapping("/settings/restore")
    Map<String, Object> restore() { return platform.restore(); }
    @GetMapping("/settings/logs")
    List<PlatformDtos.SettingsLog> settingsLogs() { return platform.settingsLogs(); }
}
