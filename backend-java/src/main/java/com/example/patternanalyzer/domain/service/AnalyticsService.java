package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.entity.AnalyticsOverviewEntity;
import com.example.patternanalyzer.domain.mapper.AnalyticsOverviewMapper;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {
    private final PlatformController platform;
    private final AnalyticsOverviewMapper mapper;

    public AnalyticsService(PlatformController platform, AnalyticsOverviewMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public AnalyticsOverviewEntity overview(String range) { return mapper.toEntity(platform.analyticsOverview(range)); }
}
