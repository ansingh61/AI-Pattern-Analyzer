package com.example.patternanalyzer.domain.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AlertService {
    private final AtomicInteger sequence = new AtomicInteger(5);
    private final Map<String, Alert> alerts = new ConcurrentHashMap<>();

    public AlertService() {
        add("alert-1", "BTCUSD", "AI Pattern Detection", "Confidence >= 85%", "1D", List.of("Desktop", "Sound"), true, Instant.parse("2025-01-01T14:23:01Z"));
        add("alert-2", "ETHUSD", "Price Below", "Price < 2500", "All", List.of("Desktop"), true, Instant.parse("2025-01-01T14:20:12Z"));
        add("alert-3", "AAPL", "Volume Spike", "Volume > 2x average", "4H", List.of("Desktop", "Email"), true, Instant.parse("2025-01-01T13:45:09Z"));
        add("alert-4", "SPY", "RSI Level", "RSI > 70", "1D", List.of("Desktop"), false, Instant.parse("2025-01-01T12:10:44Z"));
        add("alert-5", "NVDA", "% Change", "Change > 3%", "All", List.of("Desktop"), false, Instant.parse("2025-01-01T11:30:18Z"));
    }

    public List<Alert> list(String category) {
        return alerts.values().stream()
                .filter(alert -> category == null || category.isBlank() || "All Alerts".equalsIgnoreCase(category)
                        || alert.type().toLowerCase().contains(category.toLowerCase().replace(" alerts", "")))
                .sorted((left, right) -> right.id().compareTo(left.id()))
                .toList();
    }

    public Alert create(String symbol, String type, String condition, String timeframe, List<String> channels) {
        String id = "alert-" + sequence.incrementAndGet();
        Alert alert = new Alert(id, symbol.toUpperCase(), type, condition, timeframe,
                channels == null || channels.isEmpty() ? List.of("Desktop") : List.copyOf(channels), true, Instant.now());
        alerts.put(id, alert);
        return alert;
    }

    public Alert toggle(String id, boolean active) {
        Alert alert = require(id);
        Alert updated = new Alert(alert.id(), alert.symbol(), alert.type(), alert.condition(), alert.timeframe(),
                alert.channels(), active, alert.lastTriggered());
        alerts.put(id, updated);
        return updated;
    }

    public Alert test(String id) {
        Alert alert = require(id);
        Alert updated = new Alert(alert.id(), alert.symbol(), alert.type(), alert.condition(), alert.timeframe(),
                alert.channels(), alert.active(), Instant.now());
        alerts.put(id, updated);
        return updated;
    }

    public void delete(String id) {
        if (alerts.remove(id) == null) throw new IllegalArgumentException("Alert not found");
    }

    private Alert require(String id) {
        Alert alert = alerts.get(id);
        if (alert == null) throw new IllegalArgumentException("Alert not found");
        return alert;
    }

    private void add(String id, String symbol, String type, String condition, String timeframe,
                     List<String> channels, boolean active, Instant lastTriggered) {
        alerts.put(id, new Alert(id, symbol, type, condition, timeframe, channels, active, lastTriggered));
    }

    public record Alert(String id, String symbol, String type, String condition, String timeframe,
                        List<String> channels, boolean active, Instant lastTriggered) {}
}
