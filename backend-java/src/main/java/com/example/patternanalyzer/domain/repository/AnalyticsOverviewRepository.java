package com.example.patternanalyzer.domain.repository;

import com.example.patternanalyzer.domain.entity.AnalyticsOverviewEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsOverviewRepository extends JpaRepository<AnalyticsOverviewEntity, UUID> {

}