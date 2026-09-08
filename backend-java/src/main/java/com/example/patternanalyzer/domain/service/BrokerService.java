package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.entity.BrokerEntity;
import com.example.patternanalyzer.domain.mapper.BrokerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrokerService {
    private final PlatformController platform;
    private final BrokerMapper mapper;

    public BrokerService(PlatformController platform, BrokerMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public List<BrokerEntity> list() { return platform.brokers().stream().map(mapper::toEntity).toList(); }
    public BrokerEntity connect(String id) { return mapper.toEntity(platform.connect(id)); }
    public BrokerEntity disconnect(String id) { return mapper.toEntity(platform.disconnect(id)); }
    public BrokerEntity reconnect(String id) { return mapper.toEntity(platform.reconnect(id)); }
}
