package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.domain.dto.PatternResponse;
import com.example.patternanalyzer.domain.entity.PatternEntity;
import com.example.patternanalyzer.domain.mapper.PatternMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatternService {
    private final MarketDataService marketData;
    private final PatternMapper mapper;

    public PatternService(MarketDataService marketData, PatternMapper mapper) {
        this.marketData = marketData;
        this.mapper = mapper;
    }

    public List<PatternResponse> findSignals(int minConfidence, int minSimilarity, String query) {
        return marketData.scannerSignals().stream()
                .filter(signal -> signal.confidence() >= minConfidence && signal.similarity() >= minSimilarity)
                .filter(signal -> query == null || query.isBlank()
                        || (signal.symbol() + " " + signal.pattern()).toLowerCase().contains(query.toLowerCase()))
                .map(this::toEntity)
                .map(mapper::toResponse)
                .toList();
    }

    private PatternEntity toEntity(MarketDataService.ScannerSignal signal) {
        return new PatternEntity(null, signal.symbol(), signal.broker(), signal.pattern(), signal.timeframe(), signal.price(),
                signal.change(), signal.confidence(), signal.similarity(), signal.signal(), signal.volume());
    }
}
