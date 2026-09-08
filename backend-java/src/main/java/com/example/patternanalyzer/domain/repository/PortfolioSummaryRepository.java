package com.example.patternanalyzer.domain.repository;

import com.example.patternanalyzer.domain.entity.PortfolioSummaryEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioSummaryRepository extends JpaRepository<PortfolioSummaryEntity, UUID> {

}