package com.example.patternanalyzer.domain.dto;

import jakarta.validation.constraints.NotBlank;

public final class PatternDtos {
    private PatternDtos() {
    }

    public static final class Candle {
        public final int index;
        public final double open;
        public final double high;
        public final double low;
        public final double close;
        public final String timeframe;

        public Candle(int index, double open, double high, double low, double close, String timeframe) {
            this.index = index;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.timeframe = timeframe;
        }

        public int index() { return index; }
        public double open() { return open; }
        public double high() { return high; }
        public double low() { return low; }
        public double close() { return close; }
        public String timeframe() { return timeframe; }
    }

    public static final class AnalysisRequest {
        @NotBlank public String symbol;
        @NotBlank public String timeframe;
        public int start;
        public int end;

        public AnalysisRequest(String symbol, String timeframe, int start, int end) {
            this.symbol = symbol;
            this.timeframe = timeframe;
            this.start = start;
            this.end = end;
        }

        public String symbol() { return symbol; }
        public String timeframe() { return timeframe; }
        public int start() { return start; }
        public int end() { return end; }
    }

    public static final class AnalysisResult {
        public final String pattern;
        public final int confidence;
        public final int breakoutProbability;
        public final int breakdownProbability;
        public final double riskReward;
        public final double averageReturn;
        public final String recommendation;
        public final String analysisId;

        public AnalysisResult(String pattern, int confidence, int breakoutProbability, int breakdownProbability,
                              double riskReward, double averageReturn, String recommendation, String analysisId) {
            this.pattern = pattern;
            this.confidence = confidence;
            this.breakoutProbability = breakoutProbability;
            this.breakdownProbability = breakdownProbability;
            this.riskReward = riskReward;
            this.averageReturn = averageReturn;
            this.recommendation = recommendation;
            this.analysisId = analysisId;
        }

        public String pattern() { return pattern; }
        public int confidence() { return confidence; }
        public int breakoutProbability() { return breakoutProbability; }
        public int breakdownProbability() { return breakdownProbability; }
        public double riskReward() { return riskReward; }
        public double averageReturn() { return averageReturn; }
        public String recommendation() { return recommendation; }
        public String analysisId() { return analysisId; }
    }

    public static final class Match {
        public final String id;
        public final String pattern;
        public final String symbol;
        public final String date;
        public final int similarity;
        public final double outcome;
        public final int winRate;
        public final String result;
        public final String timeframe;

        public Match(String id, String pattern, String symbol, String date, int similarity, double outcome,
                     int winRate, String result, String timeframe) {
            this.id = id;
            this.pattern = pattern;
            this.symbol = symbol;
            this.date = date;
            this.similarity = similarity;
            this.outcome = outcome;
            this.winRate = winRate;
            this.result = result;
            this.timeframe = timeframe;
        }

        public String id() { return id; }
        public String pattern() { return pattern; }
        public String symbol() { return symbol; }
        public String date() { return date; }
        public int similarity() { return similarity; }
        public double outcome() { return outcome; }
        public int winRate() { return winRate; }
        public String result() { return result; }
        public String timeframe() { return timeframe; }
    }

    public static final class PaperOrder {
        @NotBlank public String symbol;
        @NotBlank public String side;
        public double quantity;

        public PaperOrder(String symbol, String side, double quantity) {
            this.symbol = symbol;
            this.side = side;
            this.quantity = quantity;
        }

        public String symbol() { return symbol; }
        public String side() { return side; }
        public double quantity() { return quantity; }
    }
}