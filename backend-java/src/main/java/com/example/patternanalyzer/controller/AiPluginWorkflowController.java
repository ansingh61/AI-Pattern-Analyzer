package com.example.patternanalyzer.controller;

import com.example.patternanalyzer.domain.dto.PlatformDtos;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class AiPluginWorkflowController {
    private final PlatformController platform;

    public AiPluginWorkflowController(PlatformController platform) { this.platform = platform; }

    @GetMapping("/ai/models")
    List<PlatformDtos.AiModel> aiModels() { return platform.aiModels(); }
    @GetMapping("/ai/datasets")
    List<PlatformDtos.AiDataset> aiDatasets() { return platform.aiDatasets(); }
    @GetMapping("/ai/evaluation")
    PlatformDtos.AiEvaluation aiEvaluation() { return platform.aiEvaluation(); }
    @GetMapping("/ai/experiments")
    List<PlatformDtos.AiExperiment> aiExperiments() { return platform.aiExperiments(); }
    @PostMapping("/ai/training")
    PlatformDtos.TrainingJob startTraining(@RequestBody(required = false) PlatformDtos.TrainingRequest request) { return platform.startTraining(request); }
    @PostMapping("/ai/training/{id}/stop")
    PlatformDtos.TrainingJob stopTraining(@PathVariable String id) { return platform.stopTraining(id); }
    @GetMapping("/ai/training/{id}/logs")
    List<PlatformDtos.TrainingLog> trainingLogs(@PathVariable String id) { return platform.trainingLogs(id); }

    @GetMapping("/plugins")
    List<PlatformDtos.Plugin> plugins(@RequestParam(defaultValue = "All") String category) { return platform.plugins(category); }
    @PostMapping("/plugins/{id}/install")
    PlatformDtos.Plugin installPlugin(@PathVariable String id) { return platform.installPlugin(id); }
    @PostMapping("/plugins/{id}/uninstall")
    PlatformDtos.Plugin uninstallPlugin(@PathVariable String id) { return platform.uninstallPlugin(id); }
    @PostMapping("/plugins/{id}/enable")
    PlatformDtos.Plugin enablePlugin(@PathVariable String id) { return platform.enablePlugin(id); }
    @PostMapping("/plugins/{id}/disable")
    PlatformDtos.Plugin disablePlugin(@PathVariable String id) { return platform.disablePlugin(id); }
    @PostMapping("/plugins/{id}/update")
    PlatformDtos.Plugin updatePlugin(@PathVariable String id) { return platform.updatePlugin(id); }
    @GetMapping("/plugins/logs")
    List<PlatformDtos.PluginLog> pluginLogs() { return platform.pluginLogs(); }

    @PostMapping("/workflow/sessions")
    PlatformDtos.WorkflowSession startWorkflow(@Valid @RequestBody PlatformDtos.WorkflowRequest request) { return platform.startWorkflow(request); }
    @PostMapping("/workflow/sessions/{id}/advance")
    PlatformDtos.WorkflowSession advanceWorkflow(@PathVariable String id, @Valid @RequestBody PlatformDtos.WorkflowAdvance request) { return platform.advanceWorkflow(id, request); }
}
