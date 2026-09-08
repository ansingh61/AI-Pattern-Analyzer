package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.PluginEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PluginMapper {
    @Mapping(target = "id", ignore = true)
    PluginEntity toEntity(PlatformDtos.Plugin source);
}
