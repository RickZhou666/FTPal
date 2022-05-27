package com.rick.ftpal.controller;

import com.rick.ftpal.domain.AnalysisEnvironmentReport;
import com.rick.ftpal.domain.AnalysisExecutionReport;
import com.rick.ftpal.service.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ftpal/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/execution/{executionId}")
    public AnalysisExecutionReport analysisExecution(@PathVariable("executionId") Integer executionId) {
        return analysisService.generateAnalysisExecutionReport(executionId);
    }

    @GetMapping("/environments")
    public AnalysisEnvironmentReport analysisEnvironment() {
        return analysisService.generateEnvironmentReport();
    }
}
