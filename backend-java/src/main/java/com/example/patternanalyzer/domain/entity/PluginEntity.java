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
@Table(name = "plugins")
public class PluginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private final UUID id;
    private final String name;
    private final String category;
    private final String version;
    private final String status;
    private final boolean enabled;
    private final double rating;
    private final int downloads;
    private final String permissions;
    private final String compatibility;

}
