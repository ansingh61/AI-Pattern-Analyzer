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
@NoArgsConstructor(force = true)
@AllArgsConstructor 
@Entity
@Table(name = "portfolio_summaries")
public class PortfolioSummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final double totalValue;
    private final double todayPnl;
    private final double totalPnl;
    private final double balance;
    private final double margin;
    private final int openPositions;
    private final int winningTrades;
    private final int losingTrades;
    private final double winRate;
    private final double averageProfit;
    private final double averageLoss;
    private final String riskScore;
    private final int aiScore;


}
