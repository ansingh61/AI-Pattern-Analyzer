package com.example.patternanalyzer.domain.dto;

import java.util.List;

public record AlertResponse(String id, String symbol, String type, String condition, String timeframe,
                            List<String> channels, boolean active, String lastTriggered) {}
