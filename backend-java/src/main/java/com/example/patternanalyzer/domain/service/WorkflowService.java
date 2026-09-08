package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.WorkflowSessionEntity;
import com.example.patternanalyzer.domain.mapper.WorkflowSessionMapper;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
    private final PlatformController platform;
    private final WorkflowSessionMapper mapper;

    public WorkflowService(PlatformController platform, WorkflowSessionMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public WorkflowSessionEntity start(PlatformDtos.WorkflowRequest request) { return mapper.toEntity(platform.startWorkflow(request)); }
    public WorkflowSessionEntity advance(String id, PlatformDtos.WorkflowAdvance request) { return mapper.toEntity(platform.advanceWorkflow(id, request)); }
}
