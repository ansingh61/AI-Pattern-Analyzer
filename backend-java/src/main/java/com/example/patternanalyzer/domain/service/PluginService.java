package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.entity.PluginEntity;
import com.example.patternanalyzer.domain.mapper.PluginMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PluginService {
    private final PlatformController platform;
    private final PluginMapper mapper;

    public PluginService(PlatformController platform, PluginMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public List<PluginEntity> list(String category) { return platform.plugins(category).stream().map(mapper::toEntity).toList(); }
    public PluginEntity install(String id) { return mapper.toEntity(platform.installPlugin(id)); }
    public PluginEntity uninstall(String id) { return mapper.toEntity(platform.uninstallPlugin(id)); }
    public PluginEntity enable(String id) { return mapper.toEntity(platform.enablePlugin(id)); }
    public PluginEntity disable(String id) { return mapper.toEntity(platform.disablePlugin(id)); }
    public PluginEntity update(String id) { return mapper.toEntity(platform.updatePlugin(id)); }
}
