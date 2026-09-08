package com.example.patternanalyzer.domain.mapper;

import com.example.patternanalyzer.domain.dto.WatchlistItemResponse;
import com.example.patternanalyzer.domain.entity.WatchlistItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchlistMapper {
    WatchlistItemResponse toResponse(WatchlistItemEntity entity);
}
