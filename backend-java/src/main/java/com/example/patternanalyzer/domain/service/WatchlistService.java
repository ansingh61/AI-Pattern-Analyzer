package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.domain.dto.WatchlistItemResponse;
import com.example.patternanalyzer.domain.entity.WatchlistItemEntity;
import com.example.patternanalyzer.domain.mapper.WatchlistMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {
    private final MarketDataService marketData;
    private final WatchlistMapper mapper;

    public WatchlistService(MarketDataService marketData, WatchlistMapper mapper) {
        this.marketData = marketData;
        this.mapper = mapper;
    }

    public List<WatchlistItemResponse> monitor(String list) {
        return marketData.watchlistMonitor(list).stream().map(this::toEntity).map(mapper::toResponse).toList();
    }

    public MarketDataService.WatchQuote add(String list, String symbol) {
        return marketData.addToWatchlist(list, symbol);
    }

    private WatchlistItemEntity toEntity(MarketDataService.WatchQuote quote) {
        return new WatchlistItemEntity(null, quote.symbol(), quote.price(), quote.change(), quote.volume(), quote.pattern(),
                quote.confidence(), quote.signal(), quote.trend(), quote.market());
    }
}
