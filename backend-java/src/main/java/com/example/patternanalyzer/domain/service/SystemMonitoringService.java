package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.entity.SystemDashboardEntity;
import com.example.patternanalyzer.domain.mapper.SystemDashboardMapper;
import org.springframework.stereotype.Service;

@Service
public class SystemMonitoringService {
    private final PlatformController platform;
    private final SystemDashboardMapper mapper;

    public SystemMonitoringService(PlatformController platform, SystemDashboardMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public SystemDashboardEntity dashboard() { return mapper.toEntity(platform.systemDashboard()); }
}
