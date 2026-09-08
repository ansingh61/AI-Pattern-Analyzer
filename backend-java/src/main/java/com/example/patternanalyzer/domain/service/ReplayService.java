package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.ReplaySessionEntity;
import com.example.patternanalyzer.domain.mapper.ReplaySessionMapper;
import org.springframework.stereotype.Service;

@Service
public class ReplayService {
    private final PlatformController platform;
    private final ReplaySessionMapper mapper;

    public ReplayService(PlatformController platform, ReplaySessionMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public ReplaySessionEntity start(PlatformDtos.ReplayRequest request) { return mapper.toEntity(platform.startReplay(request)); }
    public ReplaySessionEntity step(String id, PlatformDtos.ReplayStep request) { return mapper.toEntity(platform.stepReplay(id, request)); }
}
