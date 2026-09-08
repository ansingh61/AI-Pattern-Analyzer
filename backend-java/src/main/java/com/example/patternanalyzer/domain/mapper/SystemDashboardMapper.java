package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.SystemDashboardEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemDashboardMapper {
    @Mapping(target = "id", ignore = true)
    SystemDashboardEntity toEntity(PlatformDtos.SystemDashboard source);
}
