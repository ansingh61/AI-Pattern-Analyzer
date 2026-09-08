package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.PortfolioSummaryEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PortfolioSummaryMapper {
    @Mapping(target = "id", ignore = true)
    PortfolioSummaryEntity toEntity(PlatformDtos.PortfolioSummary source);
    PlatformDtos.PortfolioSummary toResponse(PortfolioSummaryEntity source);
}
