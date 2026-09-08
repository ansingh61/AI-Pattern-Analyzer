package com.example.patternanalyzer.domain.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
public class MarketDataService {
    private static final List<WatchQuote> QUOTES = List.of(
        new WatchQuote("BTCUSD", 43280, 2.4, "High", "Head & Shoulders", 87, "SHORT", "Bearish", "Crypto"),
        new WatchQuote("ETHUSD", 2680, -1.2, "Normal", "Bull Flag", 74, "LONG", "Bullish", "Crypto"),
        new WatchQuote("AAPL", 182.4, .8, "Low", "Triangle", 81, "WATCH", "Bullish", "Stocks"),
        new WatchQuote("SPY", 472.1, .3, "Normal", "None", 0, "WATCH", "Neutral", "Indices"),
        new WatchQuote("NVDA", 620.8, 3.1, "High", "Cup & Handle", 79, "LONG", "Bullish", "Stocks"),
        new WatchQuote("TSLA", 248.6, -2.8, "High", "Double Top", 83, "SHORT", "Bearish", "Stocks"),
        new WatchQuote("SOLUSDT", 98.4, 4.2, "High", "Ascending Triangle", 76, "LONG", "Bullish", "Crypto"),
        new WatchQuote("EURUSD", 1.0842, -.7, "Low", "Head & Shoulders", 79, "WATCH", "Bearish", "Forex"));
    private final Map<String, List<String>> watchlists = new ConcurrentHashMap<>(Map.of(
        "Default Watchlist", new ArrayList<>(List.of("BTCUSD", "ETHUSD", "AAPL", "SPY", "NVDA", "TSLA", "SOLUSDT")),
        "Crypto", new ArrayList<>(List.of("BTCUSD", "ETHUSD", "SOLUSDT")),
        "Stocks", new ArrayList<>(List.of("AAPL", "NVDA", "TSLA")),
        "Forex", new ArrayList<>(List.of("EURUSD")),
        "Indices", new ArrayList<>(List.of("SPY"))));

    public List<String> symbols() {
        return QUOTES.stream().map(WatchQuote::symbol).toList();
    }

    public List<WatchQuote> watchlistMonitor(String list) {
        List<String> symbols = watchlists.getOrDefault(list, watchlists.get("Default Watchlist"));
        return symbols.stream().map(this::quoteFor).flatMap(java.util.Optional::stream).toList();
    }

    public WatchQuote addToWatchlist(String list, String requestedSymbol) {
        String symbol = requestedSymbol == null ? "" : requestedSymbol.trim().toUpperCase().replace("/", "");
        WatchQuote quote = QUOTES.stream().filter(item -> item.symbol().equalsIgnoreCase(symbol)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown symbol: " + requestedSymbol));
        watchlists.computeIfAbsent(list, ignored -> new ArrayList<>());
        synchronized (watchlists) {
            if (!watchlists.get(list).contains(quote.symbol())) watchlists.get(list).add(quote.symbol());
        }
        return quote;
    }

    private java.util.Optional<WatchQuote> quoteFor(String symbol) {
        return QUOTES.stream().filter(item -> item.symbol().equals(symbol)).findFirst();
    }

    public List<Candle> candles(String symbol, String timeframe, int limit) {
        double price = symbol.contains("BTC") ? 43280 : symbol.contains("ETH") ? 2680 : symbol.contains("NVDA") ? 142.6 : 420;
        return IntStream.range(0, limit).mapToObj(index -> {
            double drift = Math.sin(index / 7.0) * price * 0.004;
            double open = price + drift + Math.sin(index * 1.7) * price * 0.008;
            double close = open + Math.sin(index * 0.8) * price * 0.009 + (index > limit - 14 ? price * 0.003 : 0);
            double high = Math.max(open, close) + price * 0.006;
            double low = Math.min(open, close) - price * 0.006;
            return new Candle(index, round(open), round(high), round(low), round(close), timeframe);
        }).toList();
    }

    public List<ScannerSignal> scannerSignals() {
        return List.of(
                new ScannerSignal("BTC/USDT", "Alpaca", "Bull Flag", "4H", 43280, 2.4, 94, 91, "LONG", "High"),
                new ScannerSignal("NVDA", "IBKR", "Ascending Triangle", "1D", 142.60, 1.8, 91, 88, "LONG", "High"),
                new ScannerSignal("ETH/USDT", "Binance", "Double Bottom", "4H", 2680, 3.1, 88, 84, "LONG", "Normal"),
                new ScannerSignal("SPY", "Alpaca", "Bear Flag", "1H", 584.20, -1.2, 86, 82, "SHORT", "High"),
                new ScannerSignal("SOL/USDT", "Binance", "Wedge Breakout", "15M", 188.42, 4.6, 83, 79, "LONG", "Normal"),
                new ScannerSignal("EUR/USD", "IBKR", "Head & Shoulders", "1D", 1.0842, -0.7, 79, 76, "WATCH", "Low")
        );
    }

    public Map<String, Object> systemStatus() {
        return Map.of("connection", "connected", "cpu", 14, "memoryGb", 3.8, "database", "synced", "ai", "ready", "timestamp", Instant.now());
    }

    public String orderId(String prefix) {
        return prefix + "-" + ThreadLocalRandom.current().nextInt(10000, 99999);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public record Candle(int index, double open, double high, double low, double close, String timeframe) {}
    public record ScannerSignal(String symbol, String broker, String pattern, String timeframe, double price, double change,
                                 int confidence, int similarity, String signal, String volume) {}
    public record WatchQuote(String symbol, double price, double change, String volume, String pattern, int confidence,
                             String signal, String trend, String market) {}
}
