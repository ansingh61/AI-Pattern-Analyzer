package com.example.patternanalyzer.domain.entity;

import java.time.Instant;
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
@Table(name = "replay_sessions")
public class ReplaySessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final String symbol;
    private final String timeframe;
    private final int candle;
    private final int totalCandles;
    private final String phase;
    private final int probability;
    private final int breakoutProbability;
    private final int breakdownProbability;
    private final String pattern;
    private final Instant updatedAt;

}
