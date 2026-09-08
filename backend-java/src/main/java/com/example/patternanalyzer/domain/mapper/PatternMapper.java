package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PatternResponse;
import com.example.patternanalyzer.domain.entity.PatternEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatternMapper {
    PatternResponse toResponse(PatternEntity entity);
}
