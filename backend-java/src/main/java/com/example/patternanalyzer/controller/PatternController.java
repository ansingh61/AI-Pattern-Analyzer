package com.example.patternanalyzer.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.example.patternanalyzer.domain.dto.PatternDtos.AnalysisRequest;
import com.example.patternanalyzer.domain.dto.PatternDtos.AnalysisResult;
import com.example.patternanalyzer.domain.dto.PatternDtos.Candle;
import com.example.patternanalyzer.domain.dto.PatternDtos.Match;
import com.example.patternanalyzer.domain.dto.PatternDtos.PaperOrder;

@RestController
@RequestMapping("/api")
@Validated
public class PatternController {
    private static final List<String> SYMBOLS = List.of("BTC/USDT", "ETH/USDT", "NVDA", "SPY", "SOL/USDT");

    @GetMapping("/health")
    Map<String, Object> health() {
        return Map.of("status", "ok", "service", "pattern-analyzer-api", "timestamp", Instant.now());
    }

    @GetMapping("/market/symbols")
    List<String> symbols() {
        return SYMBOLS;
    }

    @GetMapping("/market/candles")
    List<Candle> candles(@RequestParam @NotBlank String symbol,
                         @RequestParam(defaultValue = "4H") String timeframe,
                         @RequestParam(defaultValue = "72") @Min(10) @Max(240) int limit) {
        double price = symbol.contains("BTC") ? 43280 : symbol.contains("ETH") ? 2680 : 420;
        return java.util.stream.IntStream.range(0, limit)
                .mapToObj(index -> {
                    double drift = Math.sin(index / 7.0) * price * 0.004;
                    double open = price + drift + Math.sin(index * 1.7) * price * 0.008;
                    double close = open + Math.sin(index * 0.8) * price * 0.009 + (index > limit - 14 ? price * 0.003 : 0);
                    double high = Math.max(open, close) + price * 0.006;
                    double low = Math.min(open, close) - price * 0.006;
                    return new Candle(index, Math.round(open * 100.0) / 100.0, Math.round(high * 100.0) / 100.0,
                            Math.round(low * 100.0) / 100.0, Math.round(close * 100.0) / 100.0, timeframe);
                }).toList();
    }

    @PostMapping("/analysis/extract")
    AnalysisResult analyze(@RequestBody AnalysisRequest request) {
        if (request.start() < 0 || request.end() <= request.start()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selection range is invalid");
        }
        return new AnalysisResult("Bull Flag", 87, 72, 28, 2.6, 5.2,
                "Long above breakout with stop protection", "analysis-" + ThreadLocalRandom.current().nextInt(1000, 9999));
    }

    @GetMapping("/matches")
    List<Match> matches(@RequestParam(defaultValue = "Bull Flag") String pattern) {
        return List.of(
                new Match("match-001", pattern, "BTC/USDT", "2025-11-14", 94, 8.4, 72, "Breakout", "4H"),
                new Match("match-002", pattern, "ETH/USDT", "2025-08-03", 89, 5.1, 68, "Breakout", "4H"),
                new Match("match-003", pattern, "SPY", "2025-05-21", 84, -1.8, 51, "Breakdown", "1D")
        );
    }

    @PostMapping("/portfolio/paper-orders")
    Map<String, Object> paperOrder(@RequestBody PaperOrder order) {
        return Map.of("id", "paper-" + ThreadLocalRandom.current().nextInt(10000, 99999), "status", "accepted", "symbol", order.symbol(), "side", order.side());
    }

}
