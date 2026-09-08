package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.WorkflowSessionEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkflowSessionMapper {
    @Mapping(target = "id", ignore = true)
    WorkflowSessionEntity toEntity(PlatformDtos.WorkflowSession source);
}
