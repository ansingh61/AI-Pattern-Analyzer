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
@Table(name = "watchlist_items")
public class WatchlistItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final String symbol;
    private final double price;
    private final double change;
    private final String volume;
    private final String pattern;
    private final int confidence;
    private final String signal;
    private final String trend;
    private final String market;

}
