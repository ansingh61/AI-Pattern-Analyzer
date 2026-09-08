package com.example.patternanalyzer.domain.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor 
@Entity
@Table(name = "alerts")
public class AlertEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final String symbol;
    private final String type;
    private final String condition;
    private final String timeframe;
    @ElementCollection
    @CollectionTable(name = "alert_channels", joinColumns = @JoinColumn(name = "alert_id"))
    private final List<String> channels;
    private final boolean active;
    private final Instant lastTriggered;

}
