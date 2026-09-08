package com.example.patternanalyzer.controller;

import com.example.patternanalyzer.domain.dto.PlatformDtos;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class WorkspaceLibraryController {
    private final PlatformController platform;

    public WorkspaceLibraryController(PlatformController platform) { this.platform = platform; }

    @GetMapping("/workspaces/multi-chart")
    PlatformDtos.MultiChartWorkspace multiChart(@RequestParam(defaultValue = "4") int layout, @RequestParam(defaultValue = "4H") String timeframe) { return platform.multiChart(layout, timeframe); }
    @GetMapping("/library/patterns")
    List<PlatformDtos.LibraryPattern> libraryPatterns(@RequestParam(required = false) String query, @RequestParam(defaultValue = "All") String category) { return platform.libraryPatterns(query, category); }
    @GetMapping("/library/database")
    List<PlatformDtos.DatabasePattern> patternDatabase(@RequestParam(required = false) String query, @RequestParam(defaultValue = "All") String category, @RequestParam(defaultValue = "Win Rate") String sort) { return platform.patternDatabase(query, category, sort); }
    @GetMapping("/library/database/{id}/heatmap")
    List<PlatformDtos.HeatmapCell> patternHeatmap(@PathVariable String id) { return platform.patternHeatmap(id); }
    @PostMapping("/library/database/{id}/notes")
    Map<String, Object> savePatternNote(@PathVariable String id, @RequestBody Map<String, String> request) { return platform.savePatternNote(id, request); }
    @GetMapping("/library/learning")
    PlatformDtos.LearningStats libraryLearning() { return platform.libraryLearning(); }
    @PostMapping("/library/patterns/{name}/favorite")
    Map<String, Object> favoritePattern(@PathVariable String name) { return platform.favoritePattern(name); }
}
