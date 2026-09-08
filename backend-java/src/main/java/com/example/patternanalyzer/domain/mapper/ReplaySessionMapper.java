package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.ReplaySessionEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReplaySessionMapper {
    @Mapping(target = "id", ignore = true)
    ReplaySessionEntity toEntity(PlatformDtos.ReplaySession source);
}
