package com.example.patternanalyzer.domain.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "analytics_overviews")
public class AnalyticsOverviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final String range;
    private final double totalProfit;
    private final double netProfit;
    private final double winRate;
    private final double lossRate;
    private final double profitFactor;
    private final double sharpeRatio;
    private final double maximumDrawdown;
    private final double aiPredictionAccuracy;
    private final double patternSuccessRate;

}
