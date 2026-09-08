package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.AlertResponse;
import com.example.patternanalyzer.domain.entity.AlertEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    AlertResponse toResponse(AlertEntity entity);
}
