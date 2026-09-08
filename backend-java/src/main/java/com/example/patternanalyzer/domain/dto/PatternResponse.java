package com.example.patternanalyzer.domain.dto;

public record PatternResponse(String symbol, String broker, String pattern, String timeframe, double price, double change,
                               int confidence, int similarity, String signal, String volume) {}
