package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.PlatformDtos;
import com.example.patternanalyzer.domain.entity.BrokerEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrokerMapper {
    @Mapping(target = "id", ignore = true)
    BrokerEntity toEntity(PlatformDtos.Broker source);
    PlatformDtos.Broker toResponse(BrokerEntity source);
}
