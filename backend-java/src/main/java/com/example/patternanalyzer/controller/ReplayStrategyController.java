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
public class ReplayStrategyController {
    private final PlatformController platform;

    public ReplayStrategyController(PlatformController platform) { this.platform = platform; }

    @PostMapping("/replay/sessions")
    PlatformDtos.ReplaySession startReplay(@Valid @RequestBody PlatformDtos.ReplayRequest request) { return platform.startReplay(request); }
    @PostMapping("/replay/sessions/{id}/step")
    PlatformDtos.ReplaySession stepReplay(@PathVariable String id, @Valid @RequestBody PlatformDtos.ReplayStep request) { return platform.stepReplay(id, request); }
    @GetMapping("/replay/sessions/{id}/events")
    List<PlatformDtos.ReplayEvent> replayEvents(@PathVariable String id) { return platform.replayEvents(id); }
    @PostMapping("/replay/sessions/{id}/trades")
    PlatformDtos.ReplayTrade executeReplayTrade(@PathVariable String id, @Valid @RequestBody PlatformDtos.ReplayTradeRequest request) { return platform.executeReplayTrade(id, request); }
    @GetMapping("/replay/sessions/{id}/review")
    PlatformDtos.ReplayReview replayReview(@PathVariable String id) { return platform.replayReview(id); }

    @PostMapping("/strategies")
    PlatformDtos.StrategyDefinition saveStrategy(@Valid @RequestBody PlatformDtos.StrategyRequest request) { return platform.saveStrategy(request); }
    @GetMapping("/strategies/templates")
    List<PlatformDtos.StrategyTemplate> strategyTemplates() { return platform.strategyTemplates(); }
    @PostMapping("/strategies/validate")
    PlatformDtos.StrategyValidation validateStrategy(@Valid @RequestBody PlatformDtos.StrategyRequest request) { return platform.validateStrategy(request); }
    @PostMapping("/strategies/backtest")
    PlatformDtos.BacktestResult backtest(@Valid @RequestBody PlatformDtos.BacktestRequest request) { return platform.backtest(request); }
    @PostMapping("/strategies/optimize")
    Map<String, Object> optimize(@RequestBody(required = false) Map<String, Object> request) { return platform.optimize(request); }
}
