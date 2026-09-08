package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.entity.PortfolioSummaryEntity;
import com.example.patternanalyzer.domain.mapper.PortfolioSummaryMapper;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    private final PlatformController platform;
    private final PortfolioSummaryMapper mapper;

    public PortfolioService(PlatformController platform, PortfolioSummaryMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public PortfolioSummaryEntity summary() { return mapper.toEntity(platform.portfolio()); }
}
