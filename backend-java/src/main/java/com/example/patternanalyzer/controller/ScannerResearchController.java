package com.example.patternanalyzer.controller;

import com.example.patternanalyzer.domain.dto.PlatformDtos;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.example.patternanalyzer.domain.dto.PatternResponse;
import com.example.patternanalyzer.domain.service.PatternService;

@RestController
@RequestMapping("/api")
@Validated
public class ScannerResearchController {
    private final PlatformController platform;
    private final PatternService patterns;

    public ScannerResearchController(PlatformController platform, PatternService patterns) {
        this.platform = platform;
        this.patterns = patterns;
    }

    @GetMapping("/scanner/signals")
    List<PatternResponse> scannerSignals(@RequestParam(defaultValue = "0") int minConfidence, @RequestParam(defaultValue = "0") int minSimilarity, @RequestParam(required = false) String query) {
        return patterns.findSignals(minConfidence, minSimilarity, query);
    }

    @PostMapping("/scanner/runs")
    Map<String, Object> startScan(@RequestBody(required = false) PlatformDtos.ScanRequest request) { return platform.startScan(request); }

    @GetMapping("/research/summary")
    PlatformDtos.ResearchSummary researchSummary() { return platform.researchSummary(); }

    @PostMapping("/research/runs")
    Map<String, Object> runResearch(@Valid @RequestBody PlatformDtos.ResearchRequest request) { return platform.runResearch(request); }
}
