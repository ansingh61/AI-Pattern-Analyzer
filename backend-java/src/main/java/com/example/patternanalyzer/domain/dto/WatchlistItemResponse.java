package com.example.patternanalyzer.domain.dto;

public record WatchlistItemResponse(String symbol, double price, double change, String volume, String pattern,
                                    int confidence, String signal, String trend) {}
