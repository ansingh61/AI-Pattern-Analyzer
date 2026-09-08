package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.AnalyticsOverviewEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnalyticsOverviewMapper {

    @Mapping(target = "id", ignore = true)
    AnalyticsOverviewEntity toEntity(PlatformDtos.AnalyticsOverview source);
}
