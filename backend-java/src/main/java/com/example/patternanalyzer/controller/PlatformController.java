package com.example.patternanalyzer.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.example.patternanalyzer.domain.service.AlertService;
import com.example.patternanalyzer.domain.service.MarketDataService;
import com.example.patternanalyzer.domain.dto.PlatformDtos.*;


@Component
@Validated
public class PlatformController {
    private final MarketDataService marketData;
    private final AlertService alerts;

    public PlatformController(MarketDataService marketData, AlertService alerts) {
        this.marketData = marketData;
        this.alerts = alerts;
    }

    @GetMapping("/scanner/signals")
    List<MarketDataService.ScannerSignal> scannerSignals(
            @RequestParam(defaultValue = "0") int minConfidence,
            @RequestParam(defaultValue = "0") int minSimilarity,
            @RequestParam(required = false) String query) {
        return marketData.scannerSignals().stream()
                .filter(signal -> signal.confidence() >= minConfidence && signal.similarity() >= minSimilarity)
                .filter(signal -> query == null || query.isBlank()
                        || (signal.symbol() + " " + signal.pattern()).toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    @PostMapping("/scanner/runs")
    Map<String, Object> startScan(@RequestBody(required = false) ScanRequest request) {
        return Map.of("id", marketData.orderId("scan"), "status", "running", "startedAt", Instant.now(),
                "filters", request == null ? Map.of() : request);
    }

    @GetMapping("/research/summary")
    ResearchSummary researchSummary() {
        return new ResearchSummary(18492, 64.8, 7.24, 70,
                List.of(new PatternStat("Head & Shoulders", 4208, 68.4, 8.4),
                        new PatternStat("Bull Flag", 3842, 66.8, 6.8),
                        new PatternStat("Double Bottom", 2911, 64.2, 5.1)),
                "Bullish", "Moderate", "1D");
    }

    @PostMapping("/research/runs")
    Map<String, Object> runResearch(@Valid @RequestBody ResearchRequest request) {
        return Map.of("id", marketData.orderId("research"), "status", "completed", "confidence", request.minConfidence(),
                "similarity", request.minSimilarity(), "completedAt", Instant.now());
    }

    @GetMapping("/brokers")
    public List<Broker> brokers() {
        return List.of(new Broker("alpaca", "Alpaca", "connected", "paper", "Healthy", "ACC-ALP-2048", "USD", "us-east-1", 124860.42, 24180.20, 18),
            new Broker("binance", "Binance", "connected", "paper", "Healthy", "BIN-DEMO-8841", "USDT", "Singapore", 84210.18, 12040.00, 24),
            new Broker("ibkr", "Interactive Brokers", "disconnected", "live", "Needs authentication", "U123456", "USD", "New York", 0, 0, 42));
    }

    @PostMapping("/brokers/{id}/connect")
    public Broker connect(@PathVariable String id) {
        return broker(id, "connected", "Healthy");
    }

    @PostMapping("/brokers/{id}/disconnect")
    public Broker disconnect(@PathVariable String id) {
        return broker(id, "disconnected", "Offline");
    }

    @PostMapping("/brokers/{id}/reconnect")
    public Broker reconnect(@PathVariable String id) {
        return broker(id, "connected", "Healthy");
    }

    @PostMapping("/brokers/{id}/test")
    Map<String, Object> testConnection(@PathVariable String id) {
        return Map.of("brokerId", id, "status", "passed", "latencyMs", id.equals("ibkr") ? 42 : 18, "testedAt", Instant.now());
    }

    @GetMapping("/brokers/{id}/health")
    Map<String, Object> brokerHealth(@PathVariable String id) {
        return Map.of("brokerId", id, "status", id.equals("ibkr") ? "degraded" : "healthy", "latencyMs", id.equals("ibkr") ? 42 : 18,
                "marketData", "synced", "account", "synced", "orders", "synced", "checkedAt", Instant.now());
    }

    @GetMapping("/brokers/logs")
    List<BrokerLog> brokerLogs() {
        return List.of(new BrokerLog("16:42:08", "Alpaca", "Market data synchronized", "INFO"),
                new BrokerLog("16:42:05", "Binance", "Account balance refreshed", "INFO"),
                new BrokerLog("16:41:52", "IBKR", "Latency above regional threshold", "WARN"));
    }

    private Broker broker(String id, String status, String health) {
        return brokers().stream().filter(item -> item.id().equals(id)).findFirst()
                .map(item -> new Broker(item.id(), item.name(), status, item.mode(), health, item.accountId(), item.currency(), item.region(), item.availableBalance(), item.usedMargin(), item.latencyMs()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Broker not found"));
    }

    @GetMapping("/portfolio/summary")
    public PortfolioSummary portfolio() {
        return new PortfolioSummary(284750.42, 3824.18, 18420.55, 42310.80, 24180.20,
                8, 34, 12, 73.9, 824.40, -312.10, "Medium", 74);
    }

    @PostMapping("/risk/calculate")
    RiskCalculation calculateRisk(@Valid @RequestBody RiskRequest request) {
        double dollarRisk = request.balance() * request.riskPercent() / 100.0;
        double stopDistance = Math.abs(request.entry() - request.stopLoss());
        double targetDistance = Math.abs(request.target() - request.entry());
        double units = stopDistance > 0 ? Math.floor(dollarRisk / stopDistance) : 0;
        double rewardRisk = stopDistance > 0 ? targetDistance / stopDistance : 0;
        int riskScore = request.riskPercent() <= 2 ? 24 : request.riskPercent() <= 3 ? 52 : 82;
        return new RiskCalculation(units, dollarRisk, rewardRisk, units * targetDistance, stopDistance,
                targetDistance, riskScore, riskScore < 35 ? "Low Risk — Safe" : riskScore < 70 ? "Moderate Risk" : "High Risk — Caution",
                Math.min(100, 78 + (rewardRisk > 2 ? 8 : 0)), "Volatility-adjusted stop recommended");
    }

    @GetMapping("/risk/overview")
    RiskOverview riskOverview() {
        return new RiskOverview(284750.42, 8420, 14237.5, 1240, 1180, -1.8, -4.2, "Low", 3, 18.6);
    }

    @GetMapping("/portfolio/positions")
    List<Position> positions(@RequestParam(defaultValue = "All") String filter,
                             @RequestParam(required = false) String query) {
        return List.of(
                new Position("BTCUSD", "Alpaca", "LONG", 43280, 44318, 2, 41548, 46742, 2076, 2.4, 1.2, "2d 4h", 94, 91, "Bull Flag"),
                new Position("ETHUSD", "Binance", "LONG", 2680, 2734, 8, 2573, 2884, 432, 2.0, 1.5, "1d 8h", 88, 84, "Double Bottom"),
                new Position("AAPL", "IBKR", "SHORT", 228.40, 224.75, 12, 237.54, 212.41, 43.80, 1.6, 1.8, "18h", 82, 79, "Head & Shoulders"),
                new Position("NVDA", "IBKR", "LONG", 138.20, 142.60, 10, 132.67, 149.25, 44, 3.2, 1.1, "3d 2h", 91, 88, "Ascending Triangle"),
                new Position("SPY", "Alpaca", "SHORT", 588.40, 584.20, 5, 600.17, 566.12, 21, 0.7, 2.0, "9h", 86, 82, "Bear Flag"),
                new Position("SOLUSDT", "Binance", "LONG", 181.20, 188.42, 25, 173.95, 195.70, 180.50, 3.98, 1.4, "6h", 83, 79, "Wedge Breakout")
        ).stream().filter(position -> switch (filter.toUpperCase()) {
            case "LONG" -> position.direction().equals("LONG");
            case "SHORT" -> position.direction().equals("SHORT");
            case "PROFIT" -> position.pnl() >= 0;
            case "LOSS" -> position.pnl() < 0;
            default -> true;
        }).filter(position -> query == null || query.isBlank() || position.symbol().toLowerCase().contains(query.toLowerCase())).toList();
    }

    @GetMapping("/portfolio/trades")
    List<Trade> trades() {
        return List.of(new Trade("BTCUSD", "LONG", 42800, 44100, 2, 2600, "Bull Flag", "2026-09-05"),
                new Trade("NVDA", "LONG", 132.40, 138.20, 8, 46.40, "Ascending Triangle", "2026-09-02"),
                new Trade("SPY", "SHORT", 592.10, 585.40, 4, 26.80, "Bear Flag", "2026-08-29"));
    }

    @GetMapping("/portfolio/journal")
    List<JournalEntry> journal() {
        return List.of(new JournalEntry("journal-001", "BTCUSD LONG", "Bull Flag breakout", "Neutral", "Followed plan; volume confirmed entry.", "Keep waiting for confirmation", "2026-09-07"));
    }

    @PostMapping("/portfolio/journal")
    JournalEntry saveJournal(@Valid @RequestBody JournalRequest request) {
        return new JournalEntry(marketData.orderId("journal"), request.trade(), request.reason(), request.emotion(), request.notes(), request.lessons(), Instant.now().toString());
    }

    @GetMapping("/portfolio/equity")
    List<EquityPoint> equity() {
        return java.util.stream.IntStream.range(0, 30).mapToObj(index -> new EquityPoint("2026-" + String.format("%02d", Math.max(1, 9 - index / 4)) + "-" + String.format("%02d", 1 + index % 27), 245200 + index * 1280 + Math.sin(index) * 2100)).toList();
    }

    @GetMapping("/analytics/overview")
    public AnalyticsOverview analyticsOverview(@RequestParam(defaultValue = "30D") String range) {
        return new AnalyticsOverview(range, 18420.55, 18420.55, 28420.55, -10000, 73.9, 26.1,
                824.40, -312.10, 2.64, 1.86, 1.42, -4.2, 0.84, 91.3, 74.0,
                List.of(new ReturnPoint("Jan", 4.8), new ReturnPoint("Feb", 2.1), new ReturnPoint("Mar", -1.4),
                        new ReturnPoint("Apr", 6.2), new ReturnPoint("May", 3.7), new ReturnPoint("Jun", 5.4)),
                List.of(new PatternPerformance("Bull Flag", 74.3, 389, 5.2), new PatternPerformance("Double Bottom", 72.1, 214, 4.6),
                        new PatternPerformance("Head & Shoulders", 68.4, 147, 3.8)));
    }

    @GetMapping("/analytics/trades")
    List<AnalyticsTrade> analyticsTrades() {
        return List.of(new AnalyticsTrade("TR-2048", "BTCUSD", "LONG", "Bull Flag", "2026-09-05", 2600, 4.2, 2.6, "Win"),
                new AnalyticsTrade("TR-2047", "NVDA", "LONG", "Ascending Triangle", "2026-09-02", 46.40, 3.2, 1.8, "Win"),
                new AnalyticsTrade("TR-2046", "SPY", "SHORT", "Bear Flag", "2026-08-29", 26.80, 0.7, 1.4, "Win"),
                new AnalyticsTrade("TR-2045", "ETHUSD", "LONG", "Double Bottom", "2026-08-27", -312.10, -1.2, 0.9, "Loss"));
    }

    @GetMapping("/analytics/logs")
    List<AnalyticsLog> analyticsLogs() {
        return List.of(new AnalyticsLog("16:42:08", "Trade TR-2048 closed · performance metrics updated", "INFO"),
                new AnalyticsLog("16:41:52", "AI prediction accuracy recalculated · 91.3%", "INFO"),
                new AnalyticsLog("16:40:12", "Monthly report generated · September 2026", "INFO"));
    }

    @GetMapping("/settings")
    Settings settings() {
        return new Settings("dark", "English (US)", "UTC+5:30", 14, true, true, true, 72, 75, 500,
            "PatternNet v3.2", true, 2.0, 5.0, 2.0, true, true, "synced");
    }

    @PutMapping("/settings")
    Settings updateSettings(@Valid @RequestBody Settings settings) {
        return settings;
    }

    @PostMapping("/settings/backup")
    Map<String, Object> backup() {
        return Map.of("id", marketData.orderId("backup"), "status", "completed", "location", "backups/settings-latest.json", "createdAt", Instant.now());
    }

    @PostMapping("/settings/restore")
    Map<String, Object> restore() {
        return Map.of("status", "completed", "restoredFrom", "backups/settings-latest.json", "restoredAt", Instant.now());
    }

    @GetMapping("/settings/logs")
    List<SettingsLog> settingsLogs() {
        return List.of(new SettingsLog("16:42:08", "Settings loaded", "INFO"),
                new SettingsLog("16:40:12", "AI model cache refreshed", "INFO"),
                new SettingsLog("16:38:44", "Backup completed", "INFO"));
    }

    @GetMapping("/system/status")
    Map<String, Object> systemStatus() {
        return marketData.systemStatus();
    }

    @GetMapping("/system/dashboard")
    public SystemDashboard systemDashboard() {
        return new SystemDashboard(14, 48, 68, 37, 96, 3.8, 8, 42, 8.4, "synced", "connected", "connected", "connected", "running", "ready", 18, 4, 2,
            List.of(new ServiceHealth("API Gateway", "healthy", 18), new ServiceHealth("Market Data", "healthy", 24),
                new ServiceHealth("AI Engine", "healthy", 42), new ServiceHealth("Pattern Scanner", "running", 31),
                new ServiceHealth("Execution Queue", "healthy", 12), new ServiceHealth("WebSocket", "healthy", 8)));
    }

        @GetMapping("/system/timeline")
        List<SystemMetric> systemTimeline() {
        return List.of(new SystemMetric("16:30", 12, 45, 62, 31, 16), new SystemMetric("16:32", 16, 47, 65, 33, 18),
            new SystemMetric("16:34", 13, 46, 64, 34, 17), new SystemMetric("16:36", 19, 49, 69, 36, 21),
            new SystemMetric("16:38", 15, 48, 67, 35, 19), new SystemMetric("16:40", 14, 48, 68, 37, 18),
            new SystemMetric("16:42", 14, 48, 68, 37, 18));
        }

        @GetMapping("/system/alerts")
        List<SystemAlert> systemAlerts() {
        return List.of(new SystemAlert("alert-01", "Broker latency elevated", "warning", "IBKR", "16:41:52"),
            new SystemAlert("alert-02", "Execution queue within threshold", "info", "Execution Queue", "16:40:12"));
        }

    @GetMapping("/system/logs")
    List<SystemLog> systemLogs() {
        return List.of(new SystemLog("16:42:08", "INFO", "API Gateway", "Request latency within threshold"),
                new SystemLog("16:41:52", "INFO", "AI Engine", "Model cache refreshed"),
                new SystemLog("16:40:12", "WARN", "Broker IBKR", "Latency above regional threshold"));
    }

    @GetMapping("/system/jobs")
    List<BackgroundJob> backgroundJobs() {
        return List.of(new BackgroundJob("job-2048", "Pattern scan", "running", 68, "2,847 symbols"),
                new BackgroundJob("job-2047", "Analytics aggregation", "completed", 100, "18,492 trades"),
                new BackgroundJob("job-2046", "AI model sync", "queued", 0, "PatternNet v3.2"));
    }

    @GetMapping("/intelligence/news")
    List<NewsItem> news(@RequestParam(required = false) String query) {
        return List.of(new NewsItem("Bitcoin breaks above $44,000 resistance — analysts eye $50K next target", "CoinDesk", "14:23", "Bullish", "BTC/USDT", 82, 78),
                new NewsItem("Fed signals rate pause for Q1; risk assets rally on dovish tone", "Bloomberg", "14:15", "Bullish", "SPY", 74, 70),
                new NewsItem("NVIDIA reports record Q3 earnings, beats EPS by 18% on AI chip demand", "Reuters", "14:02", "Bullish", "NVDA", 88, 84),
                new NewsItem("Ethereum network congestion spikes as DeFi volume surges", "The Block", "13:50", "Neutral", "ETH/USDT", 51, 46),
                new NewsItem("Tesla shares decline on revised delivery outlook for Q4", "WSJ", "13:38", "Bearish", "TSLA", 28, 64),
                new NewsItem("Solana DEX volume hits $2.8B in 24 hours", "Decrypt", "13:20", "Bullish", "SOL/USDT", 79, 71))
                .stream().filter(item -> query == null || query.isBlank() || (item.headline() + " " + item.symbol()).toLowerCase().contains(query.toLowerCase())).toList();
    }

    @GetMapping("/intelligence/sentiment")
    SentimentSummary sentiment() {
        return new SentimentSummary(68, 62, 23, 15, "Bullish", 74, 58, 1.8, "Risk-On positioning");
    }

    @GetMapping("/intelligence/calendar")
    List<MarketEvent> calendar() {
        return List.of(new MarketEvent("15:00", "Fed Interest Rate Decision", "High", "USD", "Expected pause"),
                new MarketEvent("16:30", "NVDA Earnings Call", "High", "NVDA", "EPS beat expected"),
                new MarketEvent("18:00", "Crude Oil Inventories", "Medium", "WTI", "Forecast -1.2M"));
    }

    @GetMapping("/watchlist/monitor")
    List<WatchSymbol> watchlistMonitor(@RequestParam(defaultValue = "Default Watchlist") String list) {
        return marketData.watchlistMonitor(list).stream()
                .map(quote -> new WatchSymbol(quote.symbol(), quote.price(), quote.change(), quote.volume(), quote.pattern(),
                        quote.confidence(), quote.signal(), quote.trend())).toList();
    }

    @GetMapping("/alerts")
    List<AlertService.Alert> alerts(@RequestParam(required = false) String category) {
        return alerts.list(category);
    }

    @PostMapping("/alerts")
    AlertService.Alert createAlert(@Valid @RequestBody AlertRequest request) {
        return alerts.create(request.symbol(), request.type(), request.condition(), request.timeframe(), request.channels());
    }

    @PatchMapping("/alerts/{id}")
    AlertService.Alert toggleAlert(@PathVariable String id, @RequestBody AlertToggle request) {
        try {
            return alerts.toggle(id, request.active());
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PostMapping("/alerts/{id}/test")
    AlertService.Alert testAlert(@PathVariable String id) {
        try {
            return alerts.test(id);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @DeleteMapping("/alerts/{id}")
    void deleteAlert(@PathVariable String id) {
        try {
            alerts.delete(id);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }

    @PostMapping("/watchlist")
    Map<String, Object> addWatchlist(@RequestBody Map<String, String> request) {
        String list = request.getOrDefault("list", "Default Watchlist");
        String symbol = request.getOrDefault("symbol", "");
        try {
            MarketDataService.WatchQuote quote = marketData.addToWatchlist(list, symbol);
            return Map.of("symbol", quote.symbol(), "status", "added", "list", list);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @GetMapping("/workspaces/multi-chart")
    MultiChartWorkspace multiChart(@RequestParam(defaultValue = "4") int layout,
                                   @RequestParam(defaultValue = "4H") String timeframe) {
        List<ChartSnapshot> charts = List.of(
                new ChartSnapshot("BTC/USDT", timeframe, "Bull Flag", 43280, 2.4, 87, "EMA aligned", "RSI 64", "MACD +"),
                new ChartSnapshot("ETH/USDT", timeframe, "Double Bottom", 2680, 3.1, 84, "EMA aligned", "RSI 58", "MACD +"),
                new ChartSnapshot("NVDA", "1D", "Ascending Triangle", 142.60, 1.8, 91, "EMA bullish", "RSI 61", "MACD +"),
                new ChartSnapshot("SPY", "1H", "Bear Flag", 584.20, -1.2, 82, "EMA bearish", "RSI 42", "MACD -"));
        return new MultiChartWorkspace(Math.max(1, Math.min(6, layout)), timeframe, charts, Instant.now());
    }

    @GetMapping("/library/patterns")
    public List<LibraryPattern> libraryPatterns(@RequestParam(required = false) String query,
                                                @RequestParam(defaultValue = "All") String category) {
        return List.of(new LibraryPattern("Head & Shoulders", "Reversal", 68.4, 147, "bearish", "Classic topping reversal with three peaks", 87, 18),
                new LibraryPattern("Double Bottom", "Reversal", 72.1, 214, "bullish", "W-shaped reversal at support", 90, 14),
                new LibraryPattern("Bull Flag", "Continuation", 74.3, 389, "bullish", "Upward continuation after pullback", 92, 12),
                new LibraryPattern("Bear Flag", "Continuation", 67.9, 201, "bearish", "Downward continuation after bounce", 84, 16),
                new LibraryPattern("Ascending Triangle", "Breakout", 65.8, 178, "bullish", "Bullish breakout with rising lows", 82, 19),
                new LibraryPattern("Descending Triangle", "Breakdown", 63.2, 134, "bearish", "Bearish breakdown with falling highs", 79, 21),
                new LibraryPattern("Cup & Handle", "Continuation", 70.2, 92, "bullish", "Bullish continuation after cup formation", 86, 17))
                .stream().filter(pattern -> "All".equalsIgnoreCase(category) || pattern.category().equalsIgnoreCase(category))
                .filter(pattern -> query == null || query.isBlank() || pattern.name().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    @GetMapping("/library/database")
    List<DatabasePattern> patternDatabase(@RequestParam(required = false) String query,
                                          @RequestParam(defaultValue = "All") String category,
                                          @RequestParam(defaultValue = "Win Rate") String sort) {
        return List.of(new DatabasePattern("Head & Shoulders", "Reversal", "bearish", 87, 4208, 68.4, 3.8, 12.6, -4.1, 18, "BTC/USDT", "1D", "P-0001"),
                new DatabasePattern("Double Bottom", "Reversal", "bullish", 90, 2911, 72.1, 4.6, 14.2, -2.8, 14, "ETH/USDT", "4H", "P-0002"),
                new DatabasePattern("Bull Flag", "Continuation", "bullish", 92, 3842, 74.3, 5.2, 12.8, -2.1, 12, "BTC/USDT", "4H", "P-0003"),
                new DatabasePattern("Ascending Triangle", "Breakout", "bullish", 82, 1780, 65.8, 4.1, 11.3, -3.4, 19, "NVDA", "1D", "P-0004"),
                new DatabasePattern("Descending Triangle", "Breakdown", "bearish", 79, 1340, 63.2, 3.1, 9.2, -5.8, 21, "SPY", "1H", "P-0005"))
                .stream().filter(pattern -> "All".equalsIgnoreCase(category) || pattern.category().equalsIgnoreCase(category))
                .filter(pattern -> query == null || query.isBlank() || (pattern.name() + " " + pattern.symbol()).toLowerCase().contains(query.toLowerCase()))
                .sorted((left, right) -> "Frequency".equalsIgnoreCase(sort) ? Integer.compare(right.frequency(), left.frequency()) : Double.compare(right.winRate(), left.winRate()))
                .toList();
    }

    @GetMapping("/library/database/{id}/heatmap")
    List<HeatmapCell> patternHeatmap(@PathVariable String id) {
        return java.util.stream.IntStream.range(0, 12).mapToObj(index -> new HeatmapCell(index, 62 + (index * 7) % 35)).toList();
    }

    @PostMapping("/library/database/{id}/notes")
    Map<String, Object> savePatternNote(@PathVariable String id, @RequestBody Map<String, String> request) {
        return Map.of("patternId", id, "note", request.getOrDefault("note", ""), "savedAt", Instant.now());
    }

    @GetMapping("/library/learning")
    LearningStats libraryLearning() {
        return new LearningStats(24, 18492, 78, 92, 3.2, "PatternNet v3.2", "Learning continuously");
    }

    @PostMapping("/library/patterns/{name}/favorite")
    Map<String, Object> favoritePattern(@PathVariable String name) {
        return Map.of("pattern", name, "favorite", true, "updatedAt", Instant.now());
    }

    @PostMapping("/replay/sessions")
    public ReplaySession startReplay(@Valid @RequestBody ReplayRequest request) {
        return new ReplaySession(marketData.orderId("replay"), request.symbol(), request.timeframe(), 1, 60,
                "Formation", 74, 72, 28, "Bull Flag", Instant.now());
    }

    @PostMapping("/replay/sessions/{id}/step")
    public ReplaySession stepReplay(@PathVariable String id, @Valid @RequestBody ReplayStep request) {
        int candle = Math.max(1, Math.min(60, request.candle()));
        String phase = candle < 15 ? "Pre-Pattern" : candle < 28 ? "Formation" : candle < 42 ? "Consolidation" : candle < 45 ? "Breakout" : "Post-Breakout";
        return new ReplaySession(id, request.symbol(), request.timeframe(), candle, 60, phase, 74, 72, 28, "Bull Flag", Instant.now());
    }

    @GetMapping("/replay/sessions/{id}/events")
    List<ReplayEvent> replayEvents(@PathVariable String id) {
        return List.of(new ReplayEvent(15, "Pattern formation started", "INFO"),
                new ReplayEvent(42, "Consolidation completed", "INFO"),
                new ReplayEvent(44, "Breakout confirmation", "SIGNAL"),
                new ReplayEvent(52, "Target 1 reached", "OUTCOME"));
    }

    @PostMapping("/replay/sessions/{id}/trades")
    ReplayTrade executeReplayTrade(@PathVariable String id, @Valid @RequestBody ReplayTradeRequest request) {
        double entry = request.side().equalsIgnoreCase("LONG") ? 43280 : 44320;
        return new ReplayTrade(marketData.orderId("replay-trade"), request.side().toUpperCase(), request.candle(), entry,
                request.side().equalsIgnoreCase("LONG") ? 44100 : 43800, 3.0, "Open", Instant.now());
    }

    @GetMapping("/replay/sessions/{id}/review")
    ReplayReview replayReview(@PathVariable String id) {
        return new ReplayReview(id, 78, 3, 2.6, 72, 28,
                List.of("Wait for volume confirmation before breakout entries", "Risk was within the configured 2% limit"));
    }

    @PostMapping("/strategies")
    StrategyDefinition saveStrategy(@Valid @RequestBody StrategyRequest request) {
        return new StrategyDefinition(marketData.orderId("strategy"), request.name(), request.blocks(), "valid", Instant.now());
    }

        @GetMapping("/strategies/templates")
        List<StrategyTemplate> strategyTemplates() {
        return List.of(new StrategyTemplate("Bull Flag Breakout", List.of("Pattern Detected", "Volume Spike", "Risk Limit", "Market Order"), "Continuation", 72.4),
            new StrategyTemplate("AI Momentum Trend", List.of("AI Prediction", "EMA Trend", "Time Filter", "Trailing Stop"), "Momentum", 68.8),
            new StrategyTemplate("Mean Reversion Guard", List.of("RSI Oversold", "Support Zone", "Position Size", "Take Profit"), "Reversal", 64.2));
        }

        @PostMapping("/strategies/validate")
        StrategyValidation validateStrategy(@Valid @RequestBody StrategyRequest request) {
        boolean hasEntry = request.blocks().stream().anyMatch(block -> block.toLowerCase().contains("pattern")
            || block.toLowerCase().contains("entry") || block.toLowerCase().contains("prediction"));
        boolean hasExit = request.blocks().stream().anyMatch(block -> block.toLowerCase().contains("stop")
            || block.toLowerCase().contains("exit") || block.toLowerCase().contains("take profit"));
        List<String> errors = new java.util.ArrayList<>();
        if (!hasEntry) errors.add("Add an entry condition or AI prediction node.");
        if (!hasExit) errors.add("Add a stop loss or exit node.");
        return new StrategyValidation(errors.isEmpty() ? "valid" : "incomplete", errors,
            errors.isEmpty() ? "Strategy is ready for backtesting and deployment." : "Complete the required workflow nodes before running live.",
            Math.min(100, request.blocks().size() * 14 + (hasEntry ? 22 : 0) + (hasExit ? 22 : 0)));
        }

    @PostMapping("/strategies/backtest")
    BacktestResult backtest(@Valid @RequestBody BacktestRequest request) {
        return new BacktestResult(marketData.orderId("backtest"), "completed", 18492, 72.4, 2.31, 18.6,
                -8.2, 5.4, 1.86, 14, 82, Instant.now());
    }

    @PostMapping("/strategies/optimize")
    Map<String, Object> optimize(@RequestBody(required = false) Map<String, Object> request) {
        return Map.of("status", "completed", "improvement", "+8.4% win rate", "bestParameters", Map.of("confidence", 82, "riskReward", 2.4), "completedAt", Instant.now());
    }

    @GetMapping("/ai/models")
    List<AiModel> aiModels() {
        return List.of(new AiModel("Pattern Classifier v4.2", "active", 91.3, 84200, 100),
                new AiModel("Signal Predictor v2.1", "training", 86.4, 124800, 68),
                new AiModel("Market Regime v1.8", "ready", 87.6, 68400, 100));
    }

    @GetMapping("/ai/datasets")
    List<AiDataset> aiDatasets() {
        return List.of(new AiDataset("Pattern Outcomes 2026", 124800, "80 / 15 / 5", "synced", "EMA, RSI, MACD, volume, volatility"),
                new AiDataset("Historical Breakouts", 84200, "75 / 15 / 10", "synced", "structure, volume, trend, momentum"),
                new AiDataset("Validation Set Q3", 18720, "validation", "ready", "cross-market features"));
    }

    @GetMapping("/ai/evaluation")
    AiEvaluation aiEvaluation() {
        return new AiEvaluation(88.2, 84.7, 86.4, .921, 91.3, 8.7,
                List.of(new FeatureImportance("Volume confirmation", 92), new FeatureImportance("EMA alignment", 84),
                        new FeatureImportance("RSI momentum", 76), new FeatureImportance("Pattern shape", 71)));
    }

    @GetMapping("/ai/experiments")
    List<AiExperiment> aiExperiments() {
        return List.of(new AiExperiment("EXP-2048", "Signal Predictor v2.1", "completed", 86.4, "2026-09-07"),
                new AiExperiment("EXP-2047", "Pattern Classifier v4.2", "completed", 91.3, "2026-09-06"),
                new AiExperiment("EXP-2046", "Market Regime v1.8", "stopped", 87.6, "2026-09-04"));
    }

    @GetMapping("/plugins")
    public List<Plugin> plugins(@RequestParam(defaultValue = "All") String category) {
        return List.of(new Plugin("volume-profile", "Volume Profile Pro", "Indicators", "2.4.1", "installed", true, 4.8, 12400, "Market data", "Pattern Analyzer 1.0+"),
                new Plugin("ai-regime", "AI Market Regime", "AI Models", "1.8.0", "available", false, 4.7, 9800, "AI engine", "Pattern Analyzer 1.0+"),
                new Plugin("broker-ibkr", "Interactive Brokers Connector", "Broker Connectors", "3.1.2", "update", false, 4.5, 18400, "Broker API", "Pattern Analyzer 1.0+"),
                new Plugin("risk-widget", "Risk Dashboard Widget", "Widgets", "1.2.0", "available", false, 4.6, 7200, "Portfolio read", "Pattern Analyzer 1.0+"),
                new Plugin("strategy-pack", "Momentum Strategy Pack", "Strategies", "2.0.0", "available", false, 4.9, 21600, "Trading and broker", "Pattern Analyzer 1.0+"))
                .stream().filter(plugin -> "All".equalsIgnoreCase(category) || plugin.category().equalsIgnoreCase(category)).toList();
    }

    @PostMapping("/plugins/{id}/install")
    public Plugin installPlugin(@PathVariable String id) {
        return plugin(id, "installed", true);
    }

    @PostMapping("/plugins/{id}/uninstall")
    public Plugin uninstallPlugin(@PathVariable String id) {
        return plugin(id, "available", false);
    }

    @PostMapping("/plugins/{id}/enable")
    public Plugin enablePlugin(@PathVariable String id) {
        return plugin(id, "installed", true);
    }

    @PostMapping("/plugins/{id}/disable")
    public Plugin disablePlugin(@PathVariable String id) {
        return plugin(id, "installed", false);
    }

    @PostMapping("/plugins/{id}/update")
    public Plugin updatePlugin(@PathVariable String id) {
        return plugin(id, "installed", true);
    }

    @GetMapping("/plugins/logs")
    List<PluginLog> pluginLogs() {
        return List.of(new PluginLog("16:42:08", "Volume Profile Pro", "Extension loaded successfully", "INFO"),
                new PluginLog("16:41:52", "IBKR Connector", "Update available: 3.1.2", "UPDATE"),
                new PluginLog("16:40:12", "AI Market Regime", "Compatibility check passed", "INFO"));
    }

    private Plugin plugin(String id, String status, boolean enabled) {
        return plugins("All").stream().filter(item -> item.id().equals(id)).findFirst()
                .map(item -> new Plugin(item.id(), item.name(), item.category(), item.version(), status, enabled,
                        item.rating(), item.downloads(), item.permissions(), item.compatibility()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plugin not found"));
    }

    @PostMapping("/ai/training")
    TrainingJob startTraining(@RequestBody(required = false) TrainingRequest request) {
        return new TrainingJob(marketData.orderId("training"), "running", 34, 50, .1284, 86.4, Instant.now());
    }

    @PostMapping("/ai/training/{id}/stop")
    TrainingJob stopTraining(@PathVariable String id) {
        return new TrainingJob(id, "stopped", 34, 50, .1284, 86.4, Instant.now());
    }

    @GetMapping("/ai/training/{id}/logs")
    List<TrainingLog> trainingLogs(@PathVariable String id) {
        return List.of(new TrainingLog("16:42:08", "Dataset loaded — 124,800 samples", "INFO"),
                new TrainingLog("16:42:04", "Preprocessing and normalization complete", "INFO"),
                new TrainingLog("16:41:52", "Checkpoint saved: epoch_34.ckpt", "INFO"));
    }

    @PostMapping("/workflow/sessions")
    public WorkflowSession startWorkflow(@Valid @RequestBody WorkflowRequest request) {
        return new WorkflowSession(marketData.orderId("workflow"), "broker-connected", 1, request.symbol(), request.timeframe(),
                "Broker connected. Historical data is ready for selection.", Instant.now());
    }

    @PostMapping("/workflow/sessions/{id}/advance")
    public WorkflowSession advanceWorkflow(@PathVariable String id, @Valid @RequestBody WorkflowAdvance request) {
        List<String> steps = List.of("broker-connected", "historical-data-loaded", "pattern-selected", "pattern-extracted",
                "historical-matches-found", "statistics-generated", "recommendation-ready", "paper-trade-ready");
        int next = Math.min(steps.size() - 1, Math.max(0, request.step() + 1));
        return new WorkflowSession(id, steps.get(next), next + 1, request.symbol(), request.timeframe(),
                workflowMessage(steps.get(next)), Instant.now());
    }

    private String workflowMessage(String step) {
        return switch (step) {
            case "historical-data-loaded" -> "Historical candles loaded. Select a pattern window on the chart.";
            case "pattern-selected" -> "Pattern window captured. AI extraction is ready.";
            case "pattern-extracted" -> "Bull Flag extracted. Searching the historical database.";
            case "historical-matches-found" -> "3 historical matches found. Similarity calculation complete.";
            case "statistics-generated" -> "Breakout, breakdown, risk, reward, and duration statistics generated.";
            case "recommendation-ready" -> "AI recommendation: wait for a confirmed breakout above the flag high.";
            case "paper-trade-ready" -> "Trade setup ready. You can place an optional paper trade.";
            default -> "Workflow step complete.";
        };
    }

}
