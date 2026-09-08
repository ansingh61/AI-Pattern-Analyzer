package com.example.patternanalyzer.domain.service;

import com.example.patternanalyzer.domain.dto.AlertResponse;
import com.example.patternanalyzer.domain.entity.AlertEntity;
import com.example.patternanalyzer.domain.mapper.AlertMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertManagementService {
    private final AlertService alerts;
    private final AlertMapper mapper;

    public AlertManagementService(AlertService alerts, AlertMapper mapper) {
        this.alerts = alerts;
        this.mapper = mapper;
    }

    public List<AlertResponse> list(String category) { return alerts.list(category).stream().map(this::toEntity).map(mapper::toResponse).toList(); }
    public AlertResponse create(String symbol, String type, String condition, String timeframe, List<String> channels) { return map(alerts.create(symbol, type, condition, timeframe, channels)); }
    public AlertResponse toggle(String id, boolean active) { return map(alerts.toggle(id, active)); }
    public AlertResponse test(String id) { return map(alerts.test(id)); }
    public void delete(String id) { alerts.delete(id); }

    private AlertResponse map(AlertService.Alert alert) { return mapper.toResponse(toEntity(alert)); }
    private AlertEntity toEntity(AlertService.Alert alert) {
        return new AlertEntity(null, alert.symbol(), alert.type(), alert.condition(), alert.timeframe(),
                alert.channels(), alert.active(), alert.lastTriggered());
    }
}
