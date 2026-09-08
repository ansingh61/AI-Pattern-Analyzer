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
@Table(name = "patterns")
public class PatternEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final String symbol;
    private final String broker;
    private final String pattern;
    private final String timeframe;
    private final double price;
    private final double change;
    private final int confidence;
    private final int similarity;
    private final String signal;
    private final String volume;

}
