package com.example.patternanalyzer.controller;

import com.example.patternanalyzer.domain.dto.PlatformDtos;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.example.patternanalyzer.domain.service.AlertService;
import com.example.patternanalyzer.domain.dto.AlertResponse;
import com.example.patternanalyzer.domain.dto.WatchlistItemResponse;
import com.example.patternanalyzer.domain.service.AlertManagementService;
import com.example.patternanalyzer.domain.service.WatchlistService;
import com.example.patternanalyzer.domain.service.AlertService.Alert;

@RestController
@RequestMapping("/api")
@Validated
public class AlertWatchlistController {
    private final WatchlistService watchlists;
    private final AlertManagementService alertManagement;

    public AlertWatchlistController(WatchlistService watchlists, AlertManagementService alertManagement) {
        this.watchlists = watchlists;
        this.alertManagement = alertManagement;
    }

    @GetMapping("/watchlist/monitor")
    List<WatchlistItemResponse> watchlistMonitor(@RequestParam(defaultValue = "Default Watchlist") String list) { return watchlists.monitor(list); }
    @PostMapping("/watchlist")
    Map<String, Object> addWatchlist(@RequestBody Map<String, String> request) {
        String list = request.getOrDefault("list", "Default Watchlist");
        String symbol = request.getOrDefault("symbol", "");
        var quote = watchlists.add(list, symbol);
        return Map.of("symbol", quote.symbol(), "status", "added", "list", list);
    }

    @GetMapping("/alerts")
    List<AlertResponse> alerts(@RequestParam(required = false) String category) { return alertManagement.list(category); }
    @PostMapping("/alerts")
    AlertResponse createAlert(@Valid @RequestBody PlatformDtos.AlertRequest request) { return alertManagement.create(request.symbol(), request.type(), request.condition(), request.timeframe(), request.channels()); }
    @PatchMapping("/alerts/{id}")
    AlertResponse toggleAlert(@PathVariable String id, @RequestBody PlatformDtos.AlertToggle request) { return alertManagement.toggle(id, request.active()); }
    @PostMapping("/alerts/{id}/test")
    AlertResponse testAlert(@PathVariable String id) { return alertManagement.test(id); }
    @DeleteMapping("/alerts/{id}")
    void deleteAlert(@PathVariable String id) { alertManagement.delete(id); }
}
