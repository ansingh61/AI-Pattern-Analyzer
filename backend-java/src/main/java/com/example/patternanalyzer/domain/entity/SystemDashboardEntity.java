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
@Table(name = "system_dashboards")
public class SystemDashboardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final int cpu;
    private final int ram;
    private final int gpu;
    private final int disk;
    private final int healthScore;
    private final double memoryUsedGb;
    private final int memoryTotalGb;
    private final int diskIoMbps;
    private final double networkMbps;
    private final String database;
    private final String broker;
    private final String websocket;
    private final String tradingView;
    private final String scanner;
    private final String ai;
    private final int apiLatency;
    private final int executionQueue;
    private final int errorCount;


}
