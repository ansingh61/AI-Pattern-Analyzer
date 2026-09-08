package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.controller.PlatformController;
import com.example.patternanalyzer.domain.entity.LibraryPatternEntity;
import com.example.patternanalyzer.domain.mapper.LibraryPatternMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    private final PlatformController platform;
    private final LibraryPatternMapper mapper;

    public LibraryService(PlatformController platform, LibraryPatternMapper mapper) { this.platform = platform; this.mapper = mapper; }
    public List<LibraryPatternEntity> patterns(String query, String category) { return platform.libraryPatterns(query, category).stream().map(mapper::toEntity).toList(); }
}
