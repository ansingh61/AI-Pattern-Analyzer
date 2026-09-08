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
public class TradingController {
    private final PlatformController platform;

    public TradingController(PlatformController platform) { this.platform = platform; }

    @GetMapping("/brokers")
    List<PlatformDtos.Broker> brokers() { return platform.brokers(); }
    @PostMapping("/brokers/{id}/connect")
    PlatformDtos.Broker connect(@PathVariable String id) { return platform.connect(id); }
    @PostMapping("/brokers/{id}/disconnect")
    PlatformDtos.Broker disconnect(@PathVariable String id) { return platform.disconnect(id); }
    @PostMapping("/brokers/{id}/reconnect")
    PlatformDtos.Broker reconnect(@PathVariable String id) { return platform.reconnect(id); }
    @PostMapping("/brokers/{id}/test")
    Map<String, Object> testConnection(@PathVariable String id) { return platform.testConnection(id); }
    @GetMapping("/brokers/{id}/health")
    Map<String, Object> brokerHealth(@PathVariable String id) { return platform.brokerHealth(id); }
    @GetMapping("/brokers/logs")
    List<PlatformDtos.BrokerLog> brokerLogs() { return platform.brokerLogs(); }

    @GetMapping("/portfolio/summary")
    PlatformDtos.PortfolioSummary portfolio() { return platform.portfolio(); }
    @GetMapping("/portfolio/positions")
    List<PlatformDtos.Position> positions(@RequestParam(defaultValue = "All") String filter, @RequestParam(required = false) String query) { return platform.positions(filter, query); }
    @GetMapping("/portfolio/trades")
    List<PlatformDtos.Trade> trades() { return platform.trades(); }
    @GetMapping("/portfolio/journal")
    List<PlatformDtos.JournalEntry> journal() { return platform.journal(); }
    @PostMapping("/portfolio/journal")
    PlatformDtos.JournalEntry saveJournal(@Valid @RequestBody PlatformDtos.JournalRequest request) { return platform.saveJournal(request); }
    @GetMapping("/portfolio/equity")
    List<PlatformDtos.EquityPoint> equity() { return platform.equity(); }

    @PostMapping("/risk/calculate")
    PlatformDtos.RiskCalculation calculateRisk(@Valid @RequestBody PlatformDtos.RiskRequest request) { return platform.calculateRisk(request); }
    @GetMapping("/risk/overview")
    PlatformDtos.RiskOverview riskOverview() { return platform.riskOverview(); }
}
