package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.LibraryPatternEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryPatternMapper {
    @Mapping(target = "id", ignore = true)
    LibraryPatternEntity toEntity(PlatformDtos.LibraryPattern source);
}
